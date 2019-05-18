package com.quadrosepaineisapi.category.dto;

import com.quadrosepaineisapi.category.Category;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {

    private Long id;
    private String name;

    public Category to() {
        Category category = new Category();
        category.setName(this.name);

        return category;
    }

    public static CategoryDTO from(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName(category.getName());
        categoryDTO.setId(category.getId());

        return categoryDTO;
    }
}
