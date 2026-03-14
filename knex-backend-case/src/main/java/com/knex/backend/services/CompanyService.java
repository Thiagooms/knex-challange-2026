package com.knex.backend.services;

import com.knex.backend.dtos.company.CompanyCreateDto;
import com.knex.backend.dtos.company.CompanyResponseDto;
import com.knex.backend.entities.Company;
import com.knex.backend.entities.User;
import com.knex.backend.mappers.CompanyMapper;
import com.knex.backend.repositories.CompanyRepository;
import com.knex.backend.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final CompanyMapper companyMapper;

    public CompanyService(
            CompanyRepository companyRepository,
            UserRepository userRepository,
            CompanyMapper companyMapper
    ) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.companyMapper = companyMapper;
    }

    @Transactional
    public CompanyResponseDto createCompany(CompanyCreateDto createDto, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (user.isCollaborator()) {
            throw new RuntimeException("Usuário já possui uma empresa");
        }

        if (companyRepository.findByName(createDto.name()).isPresent()) {
            throw new RuntimeException("Empresa com este nome já existe");
        }

        Company company = new Company(createDto.name());
        Company savedCompany = companyRepository.save(company);

        user.becomeCollaborator(savedCompany);
        userRepository.save(user);

        return companyMapper.toResponseDto(savedCompany);
    }

    @Transactional(readOnly = true)
    public CompanyResponseDto getCompanyById(Long id) {
        Company company = companyRepository.findCompanyById(id)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));

        return companyMapper.toResponseDto(company);
    }

    @Transactional(readOnly = true)
    public CompanyResponseDto getUserCompany(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!user.isCollaborator()) {
            throw new RuntimeException("Usuário não possui empresa");
        }

        return companyMapper.toResponseDto(user.getCompany());
    }

    @Transactional
    public CompanyResponseDto updateCompanyName(Long id, String newName, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!user.isCollaborator()) {
            throw new RuntimeException("Usuário não é colaborador");
        }

        Company company = companyRepository.findCompanyById(id)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));

        if (!company.getId().equals(user.getCompany().getId())) {
            throw new RuntimeException("Você não tem permissão para atualizar esta empresa");
        }

        if (companyRepository.findByName(newName).isPresent()) {
            throw new RuntimeException("Empresa com este nome já existe");
        }

        company.updateName(newName);
        Company updatedCompany = companyRepository.save(company);

        return companyMapper.toResponseDto(updatedCompany);
    }
}
