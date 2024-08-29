package ra.project_module04.service;

import org.springframework.data.domain.Page;
import ra.project_module04.model.entity.Users;

import java.util.List;

public interface IUserService {
    List<Users> getAllUsers();
    Users getUserById(Long id);
    Users getUserByUserName(String username);
    Users updateUserStatus(Long id, Boolean status);
    Page<Users> getUsersWithPaginationAndSorting(Integer page, Integer pageSize, String sortBy, String orderBy, String searchName);
}
