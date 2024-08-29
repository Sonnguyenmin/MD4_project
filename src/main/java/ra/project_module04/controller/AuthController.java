package ra.project_module04.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ra.project_module04.constans.EHttpStatus;
import ra.project_module04.exception.CustomException;
import ra.project_module04.model.dto.req.FormLogin;
import ra.project_module04.model.dto.req.FormRegister;
import ra.project_module04.model.dto.resp.JwtResponse;
import ra.project_module04.model.dto.resp.ResponseWrapper;
import ra.project_module04.model.entity.Users;
import ra.project_module04.service.IAuthService;
import ra.project_module04.service.IUserService;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api.example.com/v1/auth")
public class AuthController {
    @Autowired
    private IAuthService authService;

    @Autowired
    private IUserService userService;

    @PostMapping("/sign-in")
    public ResponseEntity<?> doLogin(@Valid @RequestBody FormLogin formLogin) throws CustomException {
        Users users = userService.getUserByUserName(formLogin.getUsername());
        if (users.getStatus() == Boolean.FALSE) {
            return new ResponseEntity<>(
                    ResponseWrapper.builder()
                            .eHttpStatus(EHttpStatus.FAILED)
                            .statusCode(HttpStatus.FORBIDDEN.value())
                            .data("Tài khoản của bạn đã bị khóa")
                            .build(),
                    HttpStatus.FORBIDDEN
            );
        }
        return new ResponseEntity<>(
                ResponseWrapper.builder()
                        .eHttpStatus(EHttpStatus.SUCCESS)
                        .statusCode(HttpStatus.OK.value())
                        .data(authService.login(formLogin))
                        .build(),
                HttpStatus.OK
        );
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> doRegister(@Valid @RequestBody FormRegister formRegister) throws CustomException {
        authService.register(formRegister);
        return new ResponseEntity<>( ResponseWrapper.builder()
                .eHttpStatus(EHttpStatus.SUCCESS)
                .statusCode(HttpStatus.CREATED.value())
                .data("Tạo tài khoản thành công")
                .build(),
                HttpStatus.CREATED);
    }

}
