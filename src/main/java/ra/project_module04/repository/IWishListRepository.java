package ra.project_module04.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.project_module04.model.entity.Product;
import ra.project_module04.model.entity.Users;
import ra.project_module04.model.entity.WishList;

import java.util.List;
import java.util.Optional;

@Repository
public interface IWishListRepository extends JpaRepository<WishList, Long> {
    List<WishList> findAllByUser(Users users);
    Optional<WishList> findByIdAndUser(Long id, Users users);
    List<WishList> findByProduct(Product product);

    @Query("select w.product from WishList w group by w.product order by count(w.product) desc ")
    List<Product> findTopWishlistProducts(Pageable pageable);

    @Query("select count(w) from WishList w where w.product.id = :productId")
    Integer countUsersWhoLikedProduct(@Param("productId") Long productId);

    boolean existsByUserAndProduct(Users users, Product product);
}
