package ra.project_module04.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.project_module04.constants.OrderStatus;
import ra.project_module04.exception.CustomException;
import ra.project_module04.model.dto.req.UpdateOrderStatusReq;
import ra.project_module04.model.dto.resp.DataResponse;
import ra.project_module04.model.dto.resp.OrderResponse;
import ra.project_module04.model.entity.Order;
import ra.project_module04.service.impl.OrderServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api.example.com/v1/admin/order")
@RequiredArgsConstructor
public class OrderAdminController {
    private final OrderServiceImpl orderService;

    @GetMapping
    public ResponseEntity<DataResponse> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return new ResponseEntity<>(new DataResponse(orders, HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping("/orderById/{id}")
    public ResponseEntity<DataResponse> getOrderById(@PathVariable Long id) {
        OrderResponse orderResponse = orderService.getOrderById(id);
        return new ResponseEntity<>(new DataResponse(orderResponse, HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping("orderStatus/{status}")
    public ResponseEntity<DataResponse> getOrderByStatus(@PathVariable OrderStatus status){
        List<OrderResponse> orderResponses = orderService.getOrderResponsesByStatus(status);
        return new ResponseEntity<>(new DataResponse(orderResponses, HttpStatus.OK), HttpStatus.OK);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<DataResponse> updateOrderStatus(@PathVariable Long id, @RequestBody UpdateOrderStatusReq status) throws CustomException {
            boolean result = orderService.updateOrderStatus(id, status.getStatus());
            if (result) {
                return new ResponseEntity<>(new DataResponse("Cập nhật trạng thái đơn hàng thành công!", HttpStatus.OK), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new DataResponse("Cập nhật trạng thái đơn hàng thất bại.", HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
            }
    }

}
