package com.quadrosepaineisapi.category;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quadrosepaineisapi.category.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findById(Long id);

}