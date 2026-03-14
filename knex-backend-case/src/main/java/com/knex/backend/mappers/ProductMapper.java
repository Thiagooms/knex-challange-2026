package com.knex.backend.mappers;

import com.knex.backend.dtos.product.ProductCreateDto;
import com.knex.backend.dtos.product.ProductResponseDto;
import com.knex.backend.dtos.product.ProductUpdateDto;
import com.knex.backend.entities.Product;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {CompanyMapper.class})
public interface ProductMapper {

    @Mapping(target = "company", source = "company")
    ProductResponseDto toResponseDto(Product product);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toEntity(ProductCreateDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "stock", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "price", ignore = true)
    void updateEntity(ProductUpdateDto dto, @MappingTarget Product product);

    @AfterMapping
    default void applyUpdates(ProductUpdateDto dto, @MappingTarget Product product) {
        String name = dto.name() != null ? dto.name() : product.getName();
        String description = dto.description() != null ? dto.description() : product.getDescription();
        java.math.BigDecimal price = dto.price() != null ? dto.price() : product.getPrice();

        product.updateInfo(name, description, price);
    }
}
