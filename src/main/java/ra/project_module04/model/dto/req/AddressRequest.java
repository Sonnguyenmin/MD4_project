package ra.project_module04.model.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class AddressRequest {
    private Long id;
    private String fullAddress;
    @NotBlank(message = "Số điện thoại không được để trống")
    private String phone;
    private String receiveName;
    private Long userId;
}
