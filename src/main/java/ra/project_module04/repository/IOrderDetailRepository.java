package ra.project_module04.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.project_module04.model.entity.OrderDetails;

@Repository
public interface IOrderDetailRepository extends JpaRepository<OrderDetails, Long> {
}
