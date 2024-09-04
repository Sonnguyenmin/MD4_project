package ra.project_module04.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.project_module04.constants.OrderStatus;
import ra.project_module04.model.dto.req.OrderRequest;
import ra.project_module04.model.dto.resp.DataResponse;
import ra.project_module04.model.dto.resp.OrderConverterResponse;
import ra.project_module04.model.dto.resp.OrderResponse;
import ra.project_module04.model.entity.Order;
import ra.project_module04.service.ICartService;
import ra.project_module04.service.IOrderService;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api.example.com/v1/user")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<DataResponse> orderNow(@Valid @RequestBody OrderRequest orderRequest) {
        return new ResponseEntity<>(new DataResponse(orderService.orderNow(orderRequest), HttpStatus.OK), HttpStatus.OK);
    }

    //Lấy ra danh sách lịch sử mua hàng theo trạng thái đơn hàng
    @GetMapping("/order/historyStatus/{orderStatus}")
    public ResponseEntity<List<OrderResponse>> getOrderHistoryByStatus(@PathVariable OrderStatus orderStatus) {
        List<Order> order = orderService.getOrdersByStatus(orderStatus);
        List<OrderResponse> listOrderResponse = order.stream().map(OrderConverterResponse::changeOrderResponse)
                .collect(Collectors.toList());
        return new ResponseEntity<>(listOrderResponse, HttpStatus.OK);
    }

    //Hủy đơn hàng đang chờ xác nhận
    @PutMapping("/order/historyUser/cancel/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable Long id) {
        boolean result = orderService.cancelOrder(id);
        if (result) {
            return ResponseEntity.ok("Đơn hàng đã được hủy thành công!");
        } else {
            return ResponseEntity.badRequest().body("Không thể hủy đơn hàng. Đơn hàng không ở trạng thái chờ xác nhận.");
        }
    }

    //Lấy ra chi tiết đơn hàng theo số serial
    @GetMapping("/order/historySerial/{serialNumber}")
    public ResponseEntity<OrderResponse> getOrderHistoryBySerialNumber(@PathVariable("serialNumber") String serialNumber) {
        Order order = orderService.getOrderBySerialNumber(serialNumber);
        OrderResponse orderResponse = OrderConverterResponse.changeOrderResponse(order);
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }

    //Lấy ra danh sách lịch sử mua hàng
    @GetMapping("/order/history")
    public ResponseEntity<List<OrderResponse>> getAllOrderHistory() {
        List<Order> orders = orderService.getAllUserOrders();
        List<OrderResponse> list = orders.stream().map(OrderConverterResponse::changeOrderResponse).collect(Collectors.toList());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
