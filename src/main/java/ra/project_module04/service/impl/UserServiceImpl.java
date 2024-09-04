package ra.project_module04.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ra.project_module04.constants.RoleName;
import ra.project_module04.exception.CustomException;
import ra.project_module04.model.dto.req.UserRequest;
import ra.project_module04.model.entity.Users;
import ra.project_module04.repository.IUserRepository;
import ra.project_module04.service.IUserService;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UploadFile uploadFile;

    @Override
    public Users getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(()->new NoSuchElementException("Không tồn tại người dùng"));
    }

    @Override
    public Users getUserByUserName(String username) {
        return userRepository.findByUsername(username).orElseThrow(()->new NoSuchElementException("Không tồn tại người dùng"));
    }

    @Override
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Page<Users> getUsersWithPaginationAndSorting(Integer page, Integer pageSize, String sortBy, String orderBy, String searchName) {
        Pageable pageable;

        // Xác định cách sắp xếp
        if (!sortBy.isEmpty()) {
            Sort sort;
            switch (sortBy) {
                case "asc":
                    sort = Sort.by(orderBy).ascending();
                    break;
                case "desc":
                    sort = Sort.by(orderBy).descending();
                    break;
                default:
                    sort = Sort.by(orderBy).ascending();
            }
            pageable = PageRequest.of(page, pageSize, sort);
        } else {
            pageable = PageRequest.of(page, pageSize);
        }

        // Tìm người dùng
        Page<Users> usersPage = userRepository.findUsersByUsernameContains(searchName, pageable);

        // Kiểm tra nếu không có người dùng
        if (usersPage.isEmpty()) {
            // Bạn có thể ném ra một ngoại lệ hoặc trả về một thông điệp
            throw new NoSuchElementException("Không tìm thấy người dùng");
        }
        return usersPage;
    }

    @Override
    public Users updateUserStatus(Long id, Boolean status) throws CustomException {

        Users users = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Người dùng không tồn tại với ID: " + id));
        // Check if the user has the ROLE_ADMIN role
        boolean isAdmin = users.getRoles().stream().anyMatch(roles -> roles.getRoleName().equals(RoleName.ROLE_ADMIN));

        if (isAdmin) {
            throw new CustomException("Không thể thay đổi trạng thái của người dùng Admin có id: " + id, HttpStatus.FORBIDDEN);
        }
        users.setStatus(status);
        return userRepository.save(users);
    }

    @Override
    public boolean changePassword(String oldPassword, String newPassword, String confirmNewPassword) {
        Users currentUser = getCurrentLoggedInUser();

        if(!passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
            throw new NoSuchElementException("Mật khẩu cũ của bạn không đúng, mời bạn nhập lại !");
        }

        if(oldPassword.equals(newPassword)) {
            throw new NoSuchElementException("Mật khẩu mới đã trùng mật khẩu cũ, mời bạn nhập lại !");
        }

        if (!newPassword.equals(confirmNewPassword)) {
            throw new NoSuchElementException("Nhập lại mật khẩu không chính xác !");
        }

        currentUser.setPassword(passwordEncoder.encode(newPassword));
        currentUser.setUpdatedAt(new Date());
        userRepository.save(currentUser);
        return true;
    }

    @Override
    public Users getCurrentLoggedInUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username).orElseThrow(() -> new NoSuchElementException("Không tồn tại người dùng này!"));
    }


    @Override
    public Users updateUser(UserRequest userRequest) {
        Users updateUser = getCurrentLoggedInUser();

        if (userRequest.getFullName() != null) {
            updateUser.setFullName(userRequest.getFullName());
        }

        if (userRequest.getEmail() != null) {
            if (userRepository.existsByEmail(userRequest.getEmail())) {
                throw new NoSuchElementException("email đã tồn tại");
            }
            updateUser.setEmail(userRequest.getEmail());
        }

        if (userRequest.getPhone() != null) {
            if (userRepository.existsByPhone(userRequest.getPhone())) {
                throw new NoSuchElementException("Số điện thoại đã tồn tại");
            }
            updateUser.setPhone(userRequest.getPhone());
        }

        if (userRequest.getAddress() != null) {
            updateUser.setAddress(userRequest.getAddress());
        }

        if (userRequest.getAvatar() != null) {
            updateUser.setAvatar(uploadFile.uploadLocal(userRequest.getAvatar()));
        }

        updateUser.setUpdatedAt(new Date());

        return userRepository.save(updateUser);
    }
}
