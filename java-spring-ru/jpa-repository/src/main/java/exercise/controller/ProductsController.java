package exercise.controller;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import exercise.model.Product;
import exercise.repository.ProductRepository;
import exercise.exception.ResourceNotFoundException;


@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;

    // BEGIN

    @GetMapping
    public List<Product> findProductByPrice(@RequestParam(required = false) Integer min, @RequestParam(required = false) Integer max) {
        if (min != null && max != null)
            return productRepository.findProductByPriceBetween(min, max).stream().sorted(Comparator.comparing(Product::getPrice))
                    .collect(Collectors.toList());
        if (min != null)
            return productRepository.findAllByPriceGreaterThanEqual(min).stream().sorted(Comparator.comparing(Product::getPrice))
                    .collect(Collectors.toList());
        if (max != null)
            return productRepository.findAllByPriceLessThanEqual(max).stream().sorted(Comparator.comparing(Product::getPrice))
                    .collect(Collectors.toList());
        else return productRepository.findAll();
    }
    // END

    @GetMapping(path = "/{id}")
    public Product show(@PathVariable long id) {

        var product =  productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product with id " + id + " not found"));

        return product;
    }
}
