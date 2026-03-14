package com.knex.backend.services;

import com.knex.backend.dtos.product.ProductCreateDto;
import com.knex.backend.dtos.product.ProductResponseDto;
import com.knex.backend.dtos.product.ProductUpdateDto;
import com.knex.backend.entities.Product;
import com.knex.backend.entities.User;
import com.knex.backend.mappers.ProductMapper;
import com.knex.backend.repositories.ProductRepository;
import com.knex.backend.repositories.UserRepository;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductMapper productMapper;

    public ProductService(
            ProductRepository productRepository,
            UserRepository userRepository,
            ProductMapper productMapper
    ) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.productMapper = productMapper;
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDto> listAllProducts() {
        return productRepository.findAllActive()
                .stream()
                .map(productMapper::toResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        return productMapper.toResponseDto(product);
    }

    @Transactional
    public ProductResponseDto createProduct(ProductCreateDto createDto, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!user.isCollaborator()) {
            throw new RuntimeException("Apenas colaboradores podem criar produtos");
        }

        Product product = new Product(
                createDto.name(),
                createDto.description(),
                createDto.price(),
                createDto.stock(),
                user.getCompany()
        );

        Product savedProduct = productRepository.save(product);
        return productMapper.toResponseDto(savedProduct);
    }

    @Transactional
    public ProductResponseDto updateProduct(Long id, ProductUpdateDto updateDto, String userEmail) {
        Product product = productRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!user.isCollaborator()) {
            throw new RuntimeException("Apenas colaboradores podem atualizar produtos");
        }

        if (!product.getCompany().getId().equals(user.getCompany().getId())) {
            throw new RuntimeException("Você não tem permissão para atualizar este produto");
        }

        try {
            productMapper.updateEntity(updateDto, product);
            Product updatedProduct = productRepository.save(product);
            return productMapper.toResponseDto(updatedProduct);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new RuntimeException("Produto foi modificado por outro usuário. Tente novamente");
        }
    }

    @Transactional
    public void deleteProduct(Long id, String userEmail) {
        Product product = productRepository.findByIdAndNotDeleted(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!user.isCollaborator()) {
            throw new RuntimeException("Apenas colaboradores podem deletar produtos");
        }

        if (!product.getCompany().getId().equals(user.getCompany().getId())) {
            throw new RuntimeException("Você não tem permissão para deletar este produto");
        }

        product.softDelete();
        productRepository.save(product);
    }

    @Transactional
    public ProductResponseDto decrementStock(Long productId, Integer quantity) {
        Product product = productRepository.findByIdAndNotDeleted(productId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if (!product.hasEnoughStock(quantity)) {
            throw new RuntimeException("Stock insuficiente");
        }

        try {
            product.decrementStock(quantity);
            Product updatedProduct = productRepository.save(product);
            return productMapper.toResponseDto(updatedProduct);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new RuntimeException("Falha ao processar compra. Tente novamente");
        }
    }
}
