package com.siku.storz.service;

import com.siku.storz.dto.ProductDTO;
import com.siku.storz.model.Product;
import com.siku.storz.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.util.Assert;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class ProductService {

    private static final int THROWING_ID = 13;

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(
            readOnly = false,
            propagation = Propagation.REQUIRED
    )
    public String save(final ProductDTO productDTO) {
        log.info("Saving product in db: {}", productDTO);
        productRepository.save(new Product(productDTO.getName(), productDTO.getPrice()));
        return "Product with name" + productDTO.getName() + " saved.";
    }

    @Cacheable(cacheNames = "products", key = "#id")
    @Transactional(
            readOnly = true,
            propagation = Propagation.SUPPORTS
    )
    public ProductDTO get(final int id) {
        final Product product = getByIdOrThrow(id);
        return getProductConverter().apply(product);
    }

    @Transactional(
            readOnly = true,
            propagation = Propagation.SUPPORTS
    )
    public List<ProductDTO> getAll() {
        return StreamSupport.stream(productRepository.findAll().spliterator(), false)
                .filter(filterItem())
                .map(getProductConverter())
                .sorted(Comparator.comparing(ProductDTO::getName))
                .collect(Collectors.toList());
    }

    @Async
    public void update(final int id, final ProductDTO productDTO) {
        log.debug("Updating the product with the ID {}...", id);
        final Product product = getByIdOrThrow(id);
        productRepository.save(convertProductForUpdate().apply(productDTO, product));
    }

    public void delete(final int id) {
        productRepository.delete(getByIdOrThrow(id));
    }

    private Product getByIdOrThrow(int id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("There is no product with the id " + id));
    }

    private Function<Product, ProductDTO> getProductConverter() {
        return product -> new ProductDTO(product.getName(), product.getPrice());
    }

    private BiFunction<ProductDTO, Product, Product> convertProductForUpdate() {
        return (dto, existingProduct) -> {
            existingProduct.setName(dto.getName());
            return existingProduct;
        };
    }

    private Predicate<Product> filterItem() {
        return product -> !product.getName().isEmpty() || product.getId() < 20;
    }
}
