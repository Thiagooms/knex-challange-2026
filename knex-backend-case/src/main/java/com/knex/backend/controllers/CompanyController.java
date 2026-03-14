package com.knex.backend.controllers;

import com.knex.backend.dtos.company.CompanyCreateDto;
import com.knex.backend.dtos.company.CompanyResponseDto;
import com.knex.backend.services.CompanyService;
import com.knex.backend.utils.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;
    private final SecurityUtils securityUtils;

    public CompanyController(CompanyService companyService, SecurityUtils securityUtils) {
        this.companyService = companyService;
        this.securityUtils = securityUtils;
    }

    @PostMapping
    public ResponseEntity<CompanyResponseDto> createCompany(@Valid @RequestBody CompanyCreateDto createDto) {
        String userEmail = securityUtils.getAuthenticatedUserEmail();
        CompanyResponseDto company = companyService.createCompany(createDto, userEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(company);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponseDto> getCompanyById(@PathVariable Long id) {
        CompanyResponseDto company = companyService.getCompanyById(id);
        return ResponseEntity.ok(company);
    }

    @GetMapping("/me")
    public ResponseEntity<CompanyResponseDto> getUserCompany() {
        String userEmail = securityUtils.getAuthenticatedUserEmail();
        CompanyResponseDto company = companyService.getUserCompany(userEmail);
        return ResponseEntity.ok(company);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponseDto> updateCompanyName(
            @PathVariable Long id,
            @RequestBody UpdateCompanyNameRequest request
    ) {
        String userEmail = securityUtils.getAuthenticatedUserEmail();
        CompanyResponseDto company = companyService.updateCompanyName(id, request.name(), userEmail);
        return ResponseEntity.ok(company);
    }

    public record UpdateCompanyNameRequest(String name) {}
}
