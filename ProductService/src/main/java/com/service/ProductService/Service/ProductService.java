package com.service.ProductService.Service;

import com.service.ProductService.Model.Product;
import com.service.ProductService.Repository.ProductRepository;
import com.service.ProductService.dto.ProductRequest;
import com.service.ProductService.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductRequest pRequest) {
        Product product = Product.builder().name(pRequest.getName()).description(pRequest.getDescription())
                .price(pRequest.getPrice())
                .build();
        productRepository.save(product);
        System.out.println("Oops!!!");
        log.info("After save product {} " + product.getName());
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products= productRepository.findAll();
        return products.stream().map(product -> ProductResponse.builder().id(product.getId())
                .name(product.getName()).description(product.getDescription()).price(product.getPrice()).build()).toList();
    }
}
