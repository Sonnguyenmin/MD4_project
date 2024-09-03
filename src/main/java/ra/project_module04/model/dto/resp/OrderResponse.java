package ra.project_module04.model.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import ra.project_module04.constants.OrderStatus;
import ra.project_module04.model.entity.OrderDetails;
import ra.project_module04.model.entity.Users;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class OrderResponse {
    private Long id;

    private String serialNumber;

    private String username;

    private Long userId;

    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String receiveName;

    private String receiveAddress;

    private String receivePhone;

    private String note;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date createdAt;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date receivedAt;

    private List<OrderDetailResponse> orderDetail;
}
