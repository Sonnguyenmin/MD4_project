package ra.project_module04.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ra.project_module04.model.entity.Users;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<Users, Long> {
    //lấy thông tin ngươ dùng qua userName
    Optional<Users> findByUsername(String username);

    //@Query("select u from Users u where u.fullName like concat('%',:fullName,'%')")
    Page<Users> findUsersByUsernameContains(String fullName, Pageable pageable);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

}
