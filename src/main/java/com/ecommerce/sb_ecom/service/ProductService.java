package com.ecommerce.sb_ecom.service;

import com.ecommerce.sb_ecom.model.Product;
import com.ecommerce.sb_ecom.payload.ProductDTO;
import com.ecommerce.sb_ecom.payload.ProductResponse;


public interface ProductService {
    ProductDTO addProduct(Product product, Long categoryId);

    ProductResponse getAllProducts();
}
