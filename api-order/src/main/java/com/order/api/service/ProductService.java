package com.order.api.service;

import com.order.api.dto.request.ProductRequestDTO;
import com.order.api.dto.response.ProductResponseDTO;
import com.order.api.entity.OrderItem;
import com.order.api.entity.Product;
import com.order.api.exceptions.ResourceNotFoundException;
import com.order.api.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        Product product = new Product(productRequestDTO.name(), productRequestDTO.description(),
                productRequestDTO.linkImage(), productRequestDTO.price(),
                productRequestDTO.inventory(), productRequestDTO.brand(),
                productRequestDTO.category(), productRequestDTO.productCode());
        Product savedProduct = productRepository.save(product);
        return convertToResponseDTO(savedProduct);
    }

    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return convertToResponseDTO(product);
    }
    public List<ProductResponseDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO productRequestDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        product.setName(productRequestDTO.name());
        product.setDescription(productRequestDTO.description());
        product.setLinkImage(productRequestDTO.linkImage());
        product.setPrice(productRequestDTO.price());
        product.setInventory(productRequestDTO.inventory());
        product.setBrand(productRequestDTO.brand());
        product.setCategory(productRequestDTO.category());
        product.setProductCode(productRequestDTO.productCode());

        Product updatedProduct = productRepository.save(product);
        return convertToResponseDTO(updatedProduct);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        productRepository.delete(product);
    }

    public List<ProductResponseDTO> searchProducts(String name, String brand, String category) {
        List<Product> products = productRepository.findByNameContainingAndBrandContainingAndCategoryContaining(name, brand, category);
        return products.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    private ProductResponseDTO convertToResponseDTO(Product product) {
        return new ProductResponseDTO(product.getId(), product.getName(), product.getDescription(), product.getLinkImage(), product.getPrice(), product.getInventory(), product.getBrand(), product.getCategory(), product.getProductCode());
    }
}
