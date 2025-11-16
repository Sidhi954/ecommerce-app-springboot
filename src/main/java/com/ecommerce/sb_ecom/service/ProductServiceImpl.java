package com.ecommerce.sb_ecom.service;

import com.ecommerce.sb_ecom.model.Category;
import com.ecommerce.sb_ecom.model.Product;
import com.ecommerce.sb_ecom.payload.ProductDTO;
import com.ecommerce.sb_ecom.payload.ProductResponse;
import com.ecommerce.sb_ecom.repositories.CategoryRepository;
import com.ecommerce.sb_ecom.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public ProductDTO addProduct(Product product, Long categoryId) {
        Category category=categoryRepository.findById(categoryId)
                .orElseThrow(()->new RuntimeException("Category not found"));
        product.setImage("default.png");
        product.setCategory(category);
        double specialPrice=product.getPrice()-((product.getPrice()*0.01)* product.getPrice());
        product.setSpecialPrice(specialPrice);
        Product savedProduct=productRepository.save(product);
        return modelMapper.map(savedProduct,ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts() {
        List<Product> products=productRepository.findAll();
        List<ProductDTO> productDTOs=products.stream()
                .map(product -> modelMapper.map(product,ProductDTO.class))
                .collect(Collectors.toList());
        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDTOs);
        return productResponse;
    }

    @Override
    public ProductResponse searchByCategory(Category categoryId) {
        Category category=categoryRepository.findById(categoryId.getCategoryId())
                .orElseThrow(()->new RuntimeException("Category not found"));
        List<Product> products=productRepository.findByCategoryOrderByPriceAsc(category);
        List<ProductDTO> productDTOs=products.stream()
                .map(product -> modelMapper.map(product,ProductDTO.class))
                .collect(Collectors.toList());
        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDTOs);
        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(Product product, Long productId) {
        Product product1=productRepository.findById(productId)
                .orElseThrow(()->new RuntimeException("Product not found"));

        product1.setProductName(product.getProductName());
        product1.setProductDescription(product.getProductDescription());
        product1.setQuantity(product.getQuantity());
        product1.setPrice(product.getPrice());
        product1.setDiscount(product.getDiscount());
        product1.setSpecialPrice(product.getSpecialPrice());

        Product savedProduct=productRepository.save(product1);

        return modelMapper.map(savedProduct,ProductDTO.class);
    }
}
//I was getting error in getting the product because I was
//product1 already represents the existing product fetched from the database.
//You should never change the primary key (ID) while updating.
//If you try to set a new productId:
//JPA thinks it is a new product
//It may violate unique constraints
//It may cause a DataIntegrityViolationException
//This becomes a 500 internal server error
