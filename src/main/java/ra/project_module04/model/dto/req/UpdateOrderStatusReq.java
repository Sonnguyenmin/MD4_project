package ra.project_module04.model.dto.req;

import lombok.*;
import ra.project_module04.constants.OrderStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class UpdateOrderStatusReq {
    private OrderStatus orderStatus;
}
