package ra.project_module04.model.dto.req;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class AddressRequest {
    private Long id;
    private String fullAddress;
    private String phone;
    private String receiveName;
    private Long userId;
}
