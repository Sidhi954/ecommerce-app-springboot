package com.ecommerce.sb_ecom.service;

import com.ecommerce.sb_ecom.exceptions.APIException;
import com.ecommerce.sb_ecom.model.Category;
import com.ecommerce.sb_ecom.payload.CategoryDTO;
import com.ecommerce.sb_ecom.payload.CategoryResponse;
import com.ecommerce.sb_ecom.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;


    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getCategoryList() {
        List<Category> categoryList=categoryRepository.findAll();
        if(categoryList.isEmpty()){
            throw new APIException("No category has been created till now!!!");
        }
         List<CategoryDTO> categoryDTOS=categoryList.stream()
                 .map(c->modelMapper.map(c,CategoryDTO.class))
                 .toList();
        CategoryResponse categoryResponse=new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        return categoryResponse;
    }

    @Override
    public void createCategory(Category category) {
        Category savedCategory=categoryRepository.findByCategoryName(category.getCategoryName());
        if(savedCategory!=null){
            throw new APIException("Category already exists");
        }
        categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category category=categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Category not found"));
        categoryRepository.delete(category);
        return "Category with categoryId: " + categoryId + " deleted successfully";
    }

    @Override
    public Category updateCategory(Long categoryId, Category category) {

        Optional<Category> categories=categoryRepository.findById(categoryId);
        Category savedCategory=categories
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Category not found"));
        category.setCategoryId(categoryId);
        savedCategory=categoryRepository.save(category);
        return savedCategory;
    }
}
