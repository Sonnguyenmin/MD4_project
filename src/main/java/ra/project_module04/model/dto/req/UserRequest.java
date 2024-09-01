package ra.project_module04.model.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class UserRequest {

    @NotBlank(message = "Email không được để trống")
    @Pattern(regexp ="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "email không đúng định dạng")
    private String email;

    @NotBlank(message = "Họ và tên không được để trống")
    private String fullName;

    private MultipartFile avatar;

    @NotBlank(message = "Số điện thoại không được để trống")
    private String phone;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

//    @NotBlank(message = "Mật khẩu cũ không được để trống")
    private String oldPassword;

//    @NotBlank(message = "Mật khẩu mới không được để trống")
    private String newPassword;

//    @NotBlank(message = "Nhập Mật khẩu không được để trống")
    private String confirmNewPassword;
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date updatedAt;
}

