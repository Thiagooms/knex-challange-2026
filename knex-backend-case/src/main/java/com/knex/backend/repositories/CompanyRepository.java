package com.knex.backend.repositories;

import com.knex.backend.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByName(String name);

    @Query("SELECT c FROM Company c WHERE c.id = :id")
    Optional<Company> findCompanyById(Long id);
}
