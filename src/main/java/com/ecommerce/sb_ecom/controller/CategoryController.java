package com.ecommerce.sb_ecom.controller;

import com.ecommerce.sb_ecom.model.Category;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

public class CategoryController {
    private List<Category> categoryList = new ArrayList<>();

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public String createCategory(@RequestBody Category category) {
        categoryList.add(category);
        return "successfully added Category";
    }
}
