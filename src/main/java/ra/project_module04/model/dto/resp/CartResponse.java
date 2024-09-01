package ra.project_module04.model.dto.resp;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class CartResponse {
    private Long id;
    private Long productId;
    private String productName;
    private Integer orderQuantity;
}
