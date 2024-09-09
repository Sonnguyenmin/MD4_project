package ra.project_module04.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.project_module04.model.entity.Address;
import ra.project_module04.model.entity.Users;

import java.util.List;
import java.util.Optional;

@Repository 
public interface IAddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUsers(Users users);
    Optional<Address> findByIdAndUsers(Long id, Users users);
    boolean existsByPhone(String phone);

    @Query("select a from Address a where a.users = :user and a.isDefault = true")
    Optional<Address> findDefaultAddressByUser(@Param("user") Users user);

}
