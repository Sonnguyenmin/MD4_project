package ra.project_module04.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ra.project_module04.constants.OrderStatus;
import ra.project_module04.model.dto.resp.DataResponse;
import ra.project_module04.model.dto.resp.OrderDetailResponse;
import ra.project_module04.model.dto.resp.OrderResponse;
import ra.project_module04.model.entity.Order;
import ra.project_module04.service.impl.OrderServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api.example.com/v1/admin/order")
@RequiredArgsConstructor
public class OrderAdminController {
    private final OrderServiceImpl orderService;

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/{status}")
    public ResponseEntity<List<OrderResponse>> getOrderByStatus(@PathVariable OrderStatus status) {
        List<Order> orders = orderService.getOrdersByStatus(status);
        List<OrderResponse> orderResponses = orders.stream().map(
                order -> OrderResponse.builder()
                        .id(order.getId())
                        .username(order.getUsers().getUsername())
                        .userId(order.getUsers().getId())
                        .serialNumber(order.getSerialNumber())
                        .totalPrice(order.getTotalPrice())
                        .receiveAddress(order.getReceiveAddress())
                        .receivePhone(order.getReceivePhone())
                        .receiveName(order.getReceiveName())
                        .note(order.getNote())
                        .status(order.getStatus())
                        .createdAt(order.getCreatedAt())
                        .receivedAt(order.getReceivedAt())
                        .orderDetail(order.getOrderDetails().stream().map(
                                        detail-> OrderDetailResponse.builder()
                                                .productId(detail.getProduct().getId())
                                                .name(detail.getName())
                                                .unitPrice(detail.getUnitPrice())
                                                .quantity(detail.getOrderQuantity())
                                                .build())
                                                .collect(Collectors.toList()))
                        .build()).collect(Collectors.toList());
        return ResponseEntity.ok(orderResponses);
    }
}
