package com.knex.backend.mappers;

import com.knex.backend.dtos.company.CompanyResponseDto;
import com.knex.backend.entities.Company;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    CompanyResponseDto toResponseDto(Company company);

    Company toEntity(com.knex.backend.dtos.company.CompanyCreateDto dto);
}
