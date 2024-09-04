package ra.project_module04.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.project_module04.constants.OrderStatus;
import ra.project_module04.model.entity.Order;
import ra.project_module04.model.entity.Users;

import java.util.List;
import java.util.Optional;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUsers(Users users);
    Optional<Order> findById(Long id);
    List<Order> findByStatus(OrderStatus status);
    List<Order> findByStatusAndUsers(OrderStatus orderStatus, Users users);
    Optional<Order> findBySerialNumberAndUsers(String serialNumber, Users users);
    Optional<Order> findByIdAndUsers(Long id, Users users);
}
