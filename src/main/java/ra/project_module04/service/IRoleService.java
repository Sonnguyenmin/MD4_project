package ra.project_module04.service;

import ra.project_module04.constants.RoleName;
import ra.project_module04.model.entity.Roles;

import java.util.List;

public interface IRoleService {
    List<Roles> getAllRoles();
    Roles findByRoleName(RoleName roleName);
}
