package com.ecommerce.sb_ecom.service;

import com.ecommerce.sb_ecom.model.Category;
import com.ecommerce.sb_ecom.model.Product;
import com.ecommerce.sb_ecom.payload.ProductDTO;
import com.ecommerce.sb_ecom.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface ProductService {

    ProductDTO addProduct(Product product, Long categoryId);

    ProductResponse getAllProducts();

    ProductResponse searchByCategory(Category categoryId);

    ProductDTO updateProduct(Product product, Long productId);

    ProductDTO deleteProduct(Long productId);

    ProductResponse searchProductByKeyword(String keyword);

    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;
}
