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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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


    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product product1=productRepository.findById(productId)
                .orElseThrow(()->new RuntimeException("Product Not Found"));
        productRepository.delete(product1);
        return modelMapper.map(product1,ProductDTO.class);
    }

    @Override
    public ProductResponse searchProductByKeyword(String keyword) {
        List<Product> products=productRepository.findByProductNameLikeIgnoreCase("%"+keyword+"%");
        List<ProductDTO> productDTOS=products.stream()
                .map(product -> modelMapper.map(product,ProductDTO.class))
                .toList();
        ProductResponse productResponse=new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product productFromDb=productRepository.findById(productId)
                .orElseThrow(()->new RuntimeException("Product Not Found"));
        String path="images/";
        String fileName=uploadImage(path,image);

        productFromDb.setImage(fileName);
        Product updatedProduct=productRepository.save(productFromDb);
        return modelMapper.map(updatedProduct,ProductDTO.class);
    }

    private String uploadImage(String path, MultipartFile file) throws IOException {
        String originalFileName=file.getOriginalFilename();
        String randomId= UUID.randomUUID().toString();
        String fileName=randomId.concat(originalFileName.substring(originalFileName.lastIndexOf(".")));
        String filePath=path+ File.separator + fileName;

        File folder=new File(path);
        if(!folder.exists()){
            folder.mkdir();
        }

        Files.copy(file.getInputStream(), Paths.get(filePath));
        return fileName;
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
