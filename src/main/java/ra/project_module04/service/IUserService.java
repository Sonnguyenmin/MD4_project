package ra.project_module04.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.project_module04.exception.CustomException;
import ra.project_module04.model.dto.req.UserRequest;
import ra.project_module04.model.entity.Users;

import java.util.List;

public interface IUserService {
    Page<Users> getAllUsers(Pageable pageable, String search);
    Users getUserById(Long id);
    Users getUserByUserName(String username);
    Users updateUserStatus(Long id, Boolean status) throws CustomException;
    Page<Users> getUsersWithPaginationAndSorting(Integer page, Integer pageSize, String sortBy, String orderBy, String searchName);
    boolean changePassword(String oldPassword, String newPassword, String confirmNewPassword);
    Users getCurrentLoggedInUser();

    Users updateUser(UserRequest userRequest);
}
