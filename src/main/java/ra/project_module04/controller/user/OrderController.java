package ra.project_module04.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ra.project_module04.model.dto.req.OrderRequest;
import ra.project_module04.model.dto.resp.DataResponse;
import ra.project_module04.model.entity.Order;
import ra.project_module04.service.ICartService;
import ra.project_module04.service.IOrderService;
import ra.project_module04.service.IUserService;

@RestController
@RequestMapping("/api.example.com/v1/user")
@RequiredArgsConstructor
public class OrderController {
    private final IUserService userService;

    private final ICartService cartService;

    private final IOrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<DataResponse> orderNow(@RequestBody OrderRequest orderRequest) {
        return new ResponseEntity<>(new DataResponse(orderService.orderNow(orderRequest), HttpStatus.OK), HttpStatus.OK);
    }
}
