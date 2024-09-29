package exercise.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

import exercise.repository.ProductRepository;
import exercise.dto.ProductDTO;
import exercise.dto.ProductCreateDTO;
import exercise.dto.ProductUpdateDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.ProductMapper;

@RestController
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;

    // BEGIN
    @Autowired
    private ProductMapper productMapper;

    @GetMapping
    public List<ProductDTO> showAllProducts(){
        return productMapper.map(productRepository.findAll());
    }

    @GetMapping(path = "/{id}")
    public ProductDTO showOneProduct(@PathVariable long id){
        var product = productRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Product with id " + id + " not found"));
        return productMapper.map(product);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO create(@RequestBody ProductCreateDTO productCreateDTO){
        var product = productMapper.map(productCreateDTO);
        productRepository.save(product);
        return productMapper.map(product);
    }

    @PutMapping(path = "/{id}")
    public ProductDTO update(@PathVariable long id, @RequestBody ProductUpdateDTO productUpdateDTO){
        var product = productRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Product with id " + id + " not found"));
        productMapper.update(productUpdateDTO,product);
        productRepository.save(product);
        return productMapper.map(product);
    }
    // END
}
