package ra.project_module04.model.dto.req;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class WishListRequest {
    private Long id;
    private Long userId;
    private Long productId;
}
