package ra.project_module04.model.dto.req;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class OrderItemCart {
    private Long productId;

    private Integer quantity;
}
