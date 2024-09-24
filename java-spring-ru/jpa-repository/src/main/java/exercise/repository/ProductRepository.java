package exercise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import exercise.model.Product;

import org.springframework.data.domain.Sort;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // BEGIN
    public List<Product> findProductByPriceBetween(Integer min, Integer max);
    public List<Product> findAllByPriceGreaterThanEqual(Integer min);
    public List<Product> findAllByPriceLessThanEqual(Integer min);
    // END
}
