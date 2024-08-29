package ra.project_module04.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ra.project_module04.constans.RoleName;
import ra.project_module04.model.entity.Roles;
import ra.project_module04.repository.IRoleRepository;
import ra.project_module04.service.IRoleService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService {
    private final IRoleRepository roleRepository;

    @Override
    public List<Roles> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Roles findByRoleName(RoleName roleName) {
        return roleRepository.findByRoleName(roleName).orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò"));
    }
}
