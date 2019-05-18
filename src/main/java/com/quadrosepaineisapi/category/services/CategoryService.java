package com.quadrosepaineisapi.category.services;

import com.quadrosepaineisapi.category.Category;

public interface CategoryService {

    void create(Category category);

    Category update(Category category, Long id);

    Category findById(Long id);

}
