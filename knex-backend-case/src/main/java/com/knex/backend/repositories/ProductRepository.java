package com.knex.backend.repositories;

import com.knex.backend.entities.Company;
import com.knex.backend.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.company = :company AND p.deletedAt IS NULL")
    List<Product> findActiveByCompany(Company company);

    @Query("SELECT p FROM Product p WHERE p.deletedAt IS NULL")
    List<Product> findAllActive();

    @Query("SELECT p FROM Product p WHERE p.id = :id AND p.deletedAt IS NULL")
    Optional<Product> findByIdAndNotDeleted(Long id);
}
