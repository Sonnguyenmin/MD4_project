package ra.project_module04.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ra.project_module04.model.entity.Product;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {
    //@Query("select p from Product p where p.productName like concat('%',:productName,'%')")
    Page<Product> findAllByProductNameContains(String productName, Pageable pageable);
    boolean existsByProductName(String productName);
}
