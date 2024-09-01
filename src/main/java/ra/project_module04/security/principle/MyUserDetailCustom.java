package ra.project_module04.security.principle;


import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ra.project_module04.model.entity.Users;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

public class MyUserDetailCustom implements UserDetails {

    private Long id;

    private String username;

    private String email;

    private String fullName;

    private Boolean status;

    private String password;

    private String avatar;

    private String phone;

    private String address;

    private Date createdAt;

    private Date updatedAt;

    private Boolean isDeleted;

    private Collection<? extends GrantedAuthority> authorities;

    public static MyUserDetailCustom build(Users users){
        List<? extends GrantedAuthority> authorityList = users.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName().name())).toList();
        return MyUserDetailCustom.builder()
                .id(users.getId())
                .username(users.getUsername())
                .password(users.getPassword())
                .fullName(users.getFullName())
                .avatar(users.getAvatar())
                .phone(users.getPhone())
                .address(users.getAddress())
                .createdAt(users.getCreatedAt())
                .updatedAt(users.getUpdatedAt())
                .isDeleted(users.getIsDeleted())
                .email(users.getEmail())
                .authorities(authorityList)
                .status(users.getStatus())
                .build();
    }


    //Đối tượng quyền trong security convert từ role thành authorrity lấy quyền admin
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }


    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
