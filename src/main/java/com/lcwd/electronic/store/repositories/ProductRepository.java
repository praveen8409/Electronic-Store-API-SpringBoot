package com.lcwd.electronic.store.repositories;

import com.lcwd.electronic.store.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    // Search
    Page<Product> findByTitleContaining(String subtitle, Pageable pageable);
    Page<Product> findByLiveTrue(Pageable pageable);
}
