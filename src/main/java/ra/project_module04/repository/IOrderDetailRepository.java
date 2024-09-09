package ra.project_module04.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ra.project_module04.model.entity.OrderDetails;
import ra.project_module04.model.entity.Product;

import java.util.List;

@Repository
public interface IOrderDetailRepository extends JpaRepository<OrderDetails, Long> {
    List<OrderDetails> findByProduct(Product product);

    @Query("select od.product, sum(od.orderQuantity) AS purchaseCount " +
            "from OrderDetails od " +
            "group by od.product " +
            "ORDER BY SUM(od.orderQuantity) DESC")
    List<Object[]> findTopSellingProducts(Pageable pageable);
}
