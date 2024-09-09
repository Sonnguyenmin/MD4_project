package ra.project_module04.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.project_module04.model.dto.req.UserRequest;
import ra.project_module04.model.dto.resp.DataResponse;
import ra.project_module04.service.IUserService;

@RestController
@RequestMapping("/api.example.com/v1/user")
@RequiredArgsConstructor
public class AccountController {
    private final IUserService userService;

    @PutMapping("/account/change_password")
    public ResponseEntity<DataResponse> changePassword(@RequestBody UserRequest userRequest) {
        boolean result = userService.changePassword(userRequest.getOldPassword(), userRequest.getNewPassword(), userRequest.getConfirmNewPassword());
        if (result) {
            return new ResponseEntity<>(new DataResponse("Đổi mật khẩu thành công !!", HttpStatus.OK), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new DataResponse("Thay đổi mật khẩu thất bại",HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/account/updateProfile")
    public ResponseEntity<DataResponse> updateProfile(@Valid @ModelAttribute UserRequest userRequest) {
        userService.updateUser(userRequest);
        return new ResponseEntity<>(new DataResponse("Đã cập nhật thành công thông tin người dùng", HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping("/account")
    public ResponseEntity<DataResponse> profile() {
        return new ResponseEntity<>(new DataResponse(userService.getCurrentLoggedInUser(), HttpStatus.OK), HttpStatus.OK);
    }
}
