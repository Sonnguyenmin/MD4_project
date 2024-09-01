package ra.project_module04.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ra.project_module04.constants.RoleName;
import ra.project_module04.exception.CustomException;
import ra.project_module04.model.dto.req.FormLogin;
import ra.project_module04.model.dto.req.FormRegister;
import ra.project_module04.model.dto.resp.JwtResponse;
import ra.project_module04.model.entity.Roles;
import ra.project_module04.model.entity.Users;
import ra.project_module04.repository.IRoleRepository;
import ra.project_module04.repository.IUserRepository;
import ra.project_module04.security.jwt.JwtProvider;
import ra.project_module04.security.principle.MyUserDetailCustom;
import ra.project_module04.service.IAuthService;

import java.util.Date;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private IRoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public boolean register(FormRegister formRegister) throws CustomException {

        if (userRepository.existsByUsername(formRegister.getUsername())){
            throw new CustomException("Tên đăng nhập đã tồn tại",HttpStatus.CONFLICT);
        }

        if(userRepository.existsByPhone(formRegister.getPhone())) {
            throw new CustomException("Số điện thoại đã tồn tại", HttpStatus.CONFLICT);
        }

        if (userRepository.existsByEmail(formRegister.getEmail())) {
            throw new CustomException("Email đã tồn tại", HttpStatus.CONFLICT);
        }

        Users user = Users.builder()
                .email(formRegister.getEmail())
                .fullName(formRegister.getFullName())
                .username(formRegister.getUsername())
                .password(passwordEncoder.encode(formRegister.getPassword()))
                .phone(formRegister.getPhone())
                .status(true)
                .createdAt(new Date())
                .updatedAt(new Date())
                .isDeleted(false)
                .build();
        if (formRegister.getRoles()!=null && !formRegister.getRoles().isEmpty()){
            Set<Roles> roles = new HashSet<>();
            formRegister.getRoles().forEach(
                    role->{
                        switch (role){
                            case "ADMIN":
                                roles.add(roleRepository.findByRoleName(RoleName.ROLE_ADMIN).orElseThrow(() -> new NoSuchElementException("Vai trò không tìm thấy")));
                            case "MANAGER":
                                roles.add(roleRepository.findByRoleName(RoleName.ROLE_MANAGER).orElseThrow(() -> new NoSuchElementException("Vai trò không tìm thấy")));
                            case "USER":
                                roles.add(roleRepository.findByRoleName(RoleName.ROLE_USER).orElseThrow(() -> new NoSuchElementException("Vai trò không tìm thấy")));
                            default:
                                roles.add(roleRepository.findByRoleName(RoleName.ROLE_USER).orElseThrow(() -> new NoSuchElementException("Vai trò không tìm thấy")));
                        }
                    }
            );
            user.setRoles(roles);
        }else {
            // mac dinh la user
            Set<Roles> roles = new HashSet<>();
            roles.add(roleRepository.findByRoleName(RoleName.ROLE_USER).orElseThrow(() -> new NoSuchElementException("Vai trò không tìm thấy")));
            user.setRoles(roles);
        }
        userRepository.save(user);
        return true;
    }

    @Override
    public JwtResponse login(FormLogin formLogin) throws CustomException {
        //Xác thực username và password
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(formLogin.getUsername(), formLogin.getPassword()));
        } catch (AuthenticationException ex) {
            throw new CustomException("Tên người dùng hoặc mật khẩu không đúng",HttpStatus.BAD_REQUEST);
            //409: Lỗi không hợp lệ
        }
        MyUserDetailCustom userDetailCustom = (MyUserDetailCustom) authentication.getPrincipal();
        String accessToken = jwtProvider.generateAccessToken(userDetailCustom);
        return JwtResponse.builder()
                .accessToken(accessToken)
                .username(userDetailCustom.getUsername())
                .fullName(userDetailCustom.getFullName())
                .email(userDetailCustom.getEmail())
                .address(userDetailCustom.getAddress())
                .avatar(userDetailCustom.getAvatar())
                .createdAt(userDetailCustom.getCreatedAt())
                .updatedAt(userDetailCustom.getUpdatedAt())
                .isDeleted(userDetailCustom.getIsDeleted())
                .phone(userDetailCustom.getPhone())
                .status(userDetailCustom.getStatus())
                .roles(userDetailCustom.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()))
                .build();
    }
}
