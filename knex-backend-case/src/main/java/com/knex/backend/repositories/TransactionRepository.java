package com.knex.backend.repositories;

import com.knex.backend.entities.Product;
import com.knex.backend.entities.Transaction;
import com.knex.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUser(User user);

    List<Transaction> findByProduct(Product product);

    List<Transaction> findByUserOrderByCreatedAtDesc(User user);

    List<Transaction> findByProductOrderByCreatedAtDesc(Product product);
}
