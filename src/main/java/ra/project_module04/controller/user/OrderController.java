package ra.project_module04.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.project_module04.constants.OrderStatus;
import ra.project_module04.exception.CustomException;
import ra.project_module04.model.dto.req.OrderRequest;
import ra.project_module04.model.dto.resp.DataResponse;
import ra.project_module04.model.dto.resp.OrderConverterResponse;
import ra.project_module04.model.dto.resp.OrderResponse;
import ra.project_module04.model.entity.Order;
import ra.project_module04.service.IOrderService;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api.example.com/v1/user")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;

    @PostMapping("/cart/checkout")
    public ResponseEntity<DataResponse> orderNow(@Valid @RequestBody OrderRequest orderRequest) {
        orderService.orderNow(orderRequest);
        return new ResponseEntity<>(new DataResponse("Bạn đã đặt hàng thành công", HttpStatus.OK), HttpStatus.OK);
    }

    //Lấy ra danh sách lịch sử mua hàng theo trạng thái đơn hàng
    @GetMapping("/order/historyStatus/{orderStatus}")
    public ResponseEntity<DataResponse> getOrderHistoryByStatus(@PathVariable OrderStatus orderStatus) {
        List<Order> order = orderService.getOrdersByStatus(orderStatus);
        List<OrderResponse> listOrderResponse = order.stream().map(OrderConverterResponse::changeOrderResponse)
                .collect(Collectors.toList());
        return new ResponseEntity<>(new DataResponse(listOrderResponse, HttpStatus.OK), HttpStatus.OK);
    }

    //Hủy đơn hàng đang chờ xác nhận
    @PutMapping("/order/historyUser/cancel/{id}")
    public ResponseEntity<DataResponse> cancelOrder(@PathVariable Long id) throws CustomException {
        boolean result = orderService.cancelOrder(id);
        if (result) {
            return new ResponseEntity<>(new DataResponse("Đơn hàng đã được hủy thành công!", HttpStatus.OK), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new DataResponse("Không thể hủy đơn hàng. Đơn hàng không ở trạng thái chờ xác nhận.", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    //Lấy ra chi tiết đơn hàng theo số serial
    @GetMapping("/order/historySerial/{serialNumber}")
    public ResponseEntity<DataResponse> getOrderHistoryBySerialNumber(@PathVariable("serialNumber") String serialNumber) {
        Order order = orderService.getOrderBySerialNumber(serialNumber);
        OrderResponse orderResponse = OrderConverterResponse.changeOrderResponse(order);
        return new ResponseEntity<>(new DataResponse(orderResponse, HttpStatus.OK), HttpStatus.OK);
    }

    //Lấy ra danh sách lịch sử mua hàng
    @GetMapping("/order/history")
    public ResponseEntity<DataResponse> getAllOrderHistory() {
        List<Order> orders = orderService.getAllUserOrders();
        List<OrderResponse> list = orders.stream().map(OrderConverterResponse::changeOrderResponse).collect(Collectors.toList());
        return new ResponseEntity<>(new DataResponse(list, HttpStatus.OK), HttpStatus.OK);
    }
}
