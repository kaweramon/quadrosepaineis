package com.quadrosepaineisapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quadrosepaineisapi.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
