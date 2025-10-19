package com.ecommerce.sb_ecom.service;

import com.ecommerce.sb_ecom.model.Category;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final List<Category> categoryList = new ArrayList<>();
    private Long nextId = 1L;

    @Override
    public List<Category> getCategoryList() {
        return categoryList;
    }

    @Override
    public void createCategory(Category category) {
        category.setCategoryId(nextId++);
        categoryList.add(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category category=categoryList.stream()
                .filter(c->c.getCategoryId().equals(categoryId))
                .findFirst()
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Category not found"));

        categoryList.remove(category);
        return "Category with categoryId: " + categoryId + " deleted successfully";
    }

    @Override
    public Category updateCategory(Long categoryId, Category category) {
        Optional<Category> optionalCategory=categoryList.stream()
                .filter(c->c.getCategoryId().equals(categoryId))
                .findFirst();
        if(optionalCategory.isPresent()){
            Category updatedCategory=optionalCategory.get();
            updatedCategory.setCategoryName(category.getCategoryName());
            return updatedCategory;
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Category not found");
        }
    }
}
