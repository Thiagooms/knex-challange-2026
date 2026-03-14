package com.knex.backend.mappers;

import com.knex.backend.dtos.transaction.TransactionResponseDto;
import com.knex.backend.entities.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ProductMapper.class})
public interface TransactionMapper {

    @Mapping(target = "user", source = "user")
    @Mapping(target = "product", source = "product")
    TransactionResponseDto toResponseDto(Transaction transaction);
}
