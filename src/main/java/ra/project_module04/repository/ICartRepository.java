package ra.project_module04.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.project_module04.model.entity.Product;
import ra.project_module04.model.entity.ShoppingCart;
import ra.project_module04.model.entity.Users;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICartRepository extends JpaRepository<ShoppingCart , Long> {
    List<ShoppingCart> findAllByUsers(Users users);

    Optional<ShoppingCart> findByUsersAndProduct(Users users, Product product);

    Optional<ShoppingCart> findByIdAndUsers(Long id, Users users);

    List<ShoppingCart> findByProduct(Product product);
}