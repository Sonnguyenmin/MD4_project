package ra.project_module04.model.dto.resp;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class OrderItemCartResponse {
    private Long id;
    private String productName;
    private Integer quantity;
    private Double unitPrice;
}
