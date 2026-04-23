package com.inventory.api.service;

import com.inventory.api.dto.CategoryDto;
import com.inventory.api.dto.ProductRequest;
import com.inventory.api.dto.ProductResponse;
import com.inventory.api.model.Category;
import com.inventory.api.model.Product;
import com.inventory.api.repo.CategoryRepository;
import com.inventory.api.repo.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository,
                          CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    // =========================
    // GET PRODUCTS (FILTER + PAGINATION)
    // =========================
    public Page<ProductResponse> getProducts(
            String name,
            String category,
            Integer lowStock,
            Pageable pageable
    ) {

        Page<Product> products;

        if (name != null && !name.isBlank()) {

            products = productRepository.findByNameContainingIgnoreCase(name, pageable);

        } else if (category != null && !category.isBlank()) {

            products = productRepository.findByCategories_Name(category, pageable);

        } else if (lowStock != null) {

        products = productRepository.findByStockQuantityLessThan(lowStock, pageable);

    } else {
        products = productRepository.findAll(pageable);
    }
        return products.map(this::toResponse);
    }
    // =========================
    // CREATE PRODUCT
    // =========================
    @Transactional
    public ProductResponse create(ProductRequest request) {

        Product product = buildProduct(request);

        return toResponse(productRepository.save(product));
    }

    // =========================
    // BULK CREATE PRODUCT
    // =========================
    @Transactional
    public List<ProductResponse> bulkCreate(List<ProductRequest> requests) {

        List<Product> saved = requests.stream()
                .map(this::buildProduct)
                .map(productRepository::save)
                .toList();

        return saved.stream()
                .map(this::toResponse)
                .toList();
    }

    // =========================
    // GET BY ID
    // =========================
    public ProductResponse getById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found: " + id));

        return toResponse(product);
    }

    // =========================
    // ASSIGN / REPLACE CATEGORIES
    // =========================
    @Transactional
    public ProductResponse assignCategories(Long productId, Set<Long> categoryIds) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        Set<Category> categories = categoryIds.stream()
                .map(id -> categoryRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Category not found: " + id)))
                .collect(Collectors.toSet());

        // replace instead of add (clean many-to-many handling)
        product.setCategories(categories);

        return toResponse(productRepository.save(product));
    }

    // =========================
    // ENTITY BUILDER (REUSABLE)
    // =========================
    private Product buildProduct(ProductRequest request) {

        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setStatus(request.getStatus());

        Set<Category> categories = request.getCategoryIds().stream()
                .map(id -> categoryRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Category not found: " + id)))
                .collect(Collectors.toSet());

        product.setCategories(categories);

        return product;
    }

    // =========================
    // DTO MAPPER
    // =========================
    public ProductResponse toResponse(Product product) {

        Set<CategoryDto> categoryDtos = product.getCategories().stream()
                .map(c -> new CategoryDto(c.getId(), c.getName()))
                .collect(Collectors.toSet());

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getStatus(),
                categoryDtos
        );
    }
}