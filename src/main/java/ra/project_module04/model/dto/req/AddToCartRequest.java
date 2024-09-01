package ra.project_module04.model.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AddToCartRequest {
    private Long productId;
    private Integer quantity;

}
