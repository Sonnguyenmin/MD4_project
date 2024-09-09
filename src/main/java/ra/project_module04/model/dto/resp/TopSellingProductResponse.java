package ra.project_module04.model.dto.resp;


import lombok.*;
import ra.project_module04.model.entity.Product;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TopSellingProductResponse {
    private Product product;
    private Long purchaseCount;
}
