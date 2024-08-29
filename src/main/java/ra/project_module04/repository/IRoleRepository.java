package ra.project_module04.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.project_module04.constans.RoleName;
import ra.project_module04.model.entity.Roles;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Roles, Long> {
    Optional<Roles> findByRoleName(RoleName roleName);
}
