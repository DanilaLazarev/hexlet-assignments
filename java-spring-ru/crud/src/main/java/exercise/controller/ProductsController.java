package exercise.controller;

import java.util.List;

import exercise.dto.ProductCreateDTO;
import exercise.dto.ProductDTO;
import exercise.dto.ProductUpdateDTO;
import exercise.mapper.CategoryMapper;
import exercise.mapper.ProductMapper;
import exercise.model.Category;
import exercise.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import exercise.exception.ResourceNotFoundException;
import exercise.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping("/products")
public class ProductsController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;


    // BEGIN
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @GetMapping
    public List<ProductDTO> index(){
        return productRepository.findAll().stream().map(p -> productMapper.map(p)).toList();
    }

    @GetMapping("/{id}")
    public ProductDTO show(@PathVariable Long id){
        var product = productRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Product with id " + id + " not exist"));
        return productMapper.map(product);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> create(@Valid @RequestBody ProductCreateDTO createDTO){
        Category category;
        if(categoryRepository.findById(createDTO.getCategoryId()).isPresent()){
            category = categoryRepository.findById(createDTO.getCategoryId()).get();
            var product = productMapper.map(createDTO);
            product.setCategory(category);
            productRepository.save(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(productMapper.map(product));
        }
        else{
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ProductDTO update(@PathVariable Long id,@Valid @RequestBody ProductUpdateDTO updateDTO){
        var product = productRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Product with id " + id + " not exist"));
        productMapper.update(updateDTO,product);
        if(!(updateDTO.getCategoryId()==null)){
                    var category = categoryRepository.findById(updateDTO.getCategoryId().get()).orElseThrow(() -> new ResourceNotFoundException("Category with id " + updateDTO.getCategoryId().get() + " not exist"));
                    product.setCategory(category);
                }
        productRepository.save(product);
        return productMapper.map(product);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        productRepository.deleteById(id);
    }
    // END
}
