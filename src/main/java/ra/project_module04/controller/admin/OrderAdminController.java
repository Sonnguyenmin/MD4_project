package ra.project_module04.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.project_module04.constants.OrderStatus;
import ra.project_module04.model.dto.req.UDOrderStatusReq;
import ra.project_module04.model.dto.resp.OrderDetailResponse;
import ra.project_module04.model.dto.resp.OrderResponse;
import ra.project_module04.model.entity.Order;
import ra.project_module04.service.impl.OrderServiceImpl;

import java.util.List;
import java.util.NoSuchElementException;
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

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        OrderResponse orderResponse = orderService.getOrderById(id);
        return ResponseEntity.ok(orderResponse);
    }


//    @GetMapping("/{status}")
//    public ResponseEntity<List<OrderResponse>> getOrderByStatus(@PathVariable OrderStatus status){
//        List<OrderResponse> orderResponses = orderService.getOrderResponsesByStatus(status);
//        return ResponseEntity.ok(orderResponses);
//    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateOrderStatus( @PathVariable Long id, @RequestBody UDOrderStatusReq status) {
        try {
            boolean result = orderService.updateOrderStatus(id, status.getStatus());

            if (result) {
                return ResponseEntity.ok("Cập nhật trạng thái đơn hàng thành công!");
            } else {
                return ResponseEntity.badRequest().body("Cập nhật trạng thái đơn hàng thất bại.");
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Có lỗi xảy ra trong quá trình cập nhật trạng thái.");
        }
    }

}
