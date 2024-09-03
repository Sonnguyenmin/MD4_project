package ra.project_module04.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.project_module04.model.entity.Users;
import ra.project_module04.model.entity.WishList;

import java.util.List;
import java.util.Optional;

@Repository
public interface IWishListRepository extends JpaRepository<WishList, Long> {
    List<WishList> findAllByUser(Users users);
    Optional<WishList> findByIdAndUser(Long id, Users users);
}
