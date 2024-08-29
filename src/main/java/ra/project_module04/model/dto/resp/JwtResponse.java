package ra.project_module04.model.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class JwtResponse {
    private final String type = "Bearer";//kiểu mã hóa thông tin
    private String accessToken; //cho phép người dùng truy cập tài nguyên ko
    private String username;
    private String email;
    private String fullName;
    private Boolean status;
    private String avatar;
    private String phone;
    private String address;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date createdAt;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date updatedAt;
    private Boolean isDeleted;
    private Set<String> roles;
}
