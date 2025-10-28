package com.ecommerce.sb_ecom.service;

import com.ecommerce.sb_ecom.model.Category;
import com.ecommerce.sb_ecom.payload.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse getCategoryList();
    void createCategory(Category category);

    String deleteCategory(Long categoryId);

    Category updateCategory(Long categoryId, Category category);
}
