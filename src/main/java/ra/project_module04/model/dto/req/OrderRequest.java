package ra.project_module04.model.dto.req;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder


public class OrderRequest {
    private Long userId;
    private List<OrderItemCart> itemsCart;
    private String receiveName;
    private String receiveAddress;
    private String receivePhone;
    private String note;
}
