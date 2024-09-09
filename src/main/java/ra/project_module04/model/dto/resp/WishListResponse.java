package ra.project_module04.model.dto.resp;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class WishListResponse {
    private Long id;
    private String wishlistProName;
    private Long userId;
    private Long productId;

}
