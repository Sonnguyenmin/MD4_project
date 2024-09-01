package ra.project_module04.service.impl;

import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import ra.project_module04.constants.OrderStatus;
import ra.project_module04.model.dto.req.OrderItemCart;
import ra.project_module04.model.dto.req.OrderRequest;
import ra.project_module04.model.entity.Order;
import ra.project_module04.model.entity.OrderDetails;
import ra.project_module04.model.entity.Product;
import ra.project_module04.model.entity.Users;
import ra.project_module04.repository.IOrderDetailRepository;
import ra.project_module04.repository.IOrderRepository;
import ra.project_module04.repository.IProductRepository;
import ra.project_module04.service.IOrderService;
import ra.project_module04.service.IUserService;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {
    private final IOrderRepository orderRepository;

    private final IProductRepository productRepository;

    private final IUserService userService;

    private final IOrderDetailRepository orderDetailRepository;

    @Override
    public Order orderNow(OrderRequest orderRequest) {
        Users user = userService.getCurrentLoggedInUser();
        Order order = Order.builder()
                .users(user)
                .serialNumber(UUID.randomUUID().toString())
                .totalPrice(calculateTotalPrice(orderRequest.getItemsCart()))
                .status(OrderStatus.WAITING)
                .receiveAddress(orderRequest.getReceiveAddress())
                .receivePhone(orderRequest.getReceivePhone())
                .receiveName(orderRequest.getReceiveName())
                .note(orderRequest.getNote())
                .createdAt(new Date())
                .receivedAt(new Date())
                .build();
        order = orderRepository.save(order);

        Order findOder = order;
        List<OrderDetails> list = orderRequest.getItemsCart().stream().map(
                item->{
                    Product product = productRepository.findById(item.getProductId())
                            .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

                    return OrderDetails.builder()
                            .order(findOder)
                            .product(product)
                            .name(product.getProductName())
                            .unitPrice(product.getUnitPrice())
                            .orderQuantity(item.getQuantity())
                            .build();
                }).collect(Collectors.toList());
        List<OrderDetails> orderDetails = orderDetailRepository.saveAll(list);
        order.setOrderDetails(orderDetails);
        return order;
    }

    private Double calculateTotalPrice(List<OrderItemCart> orderItem) {
        return orderItem.stream()
                .mapToDouble(item-> {
                    Product product = productRepository.findById(item.getProductId())
                            .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));
                    return product.getUnitPrice() * item.getQuantity();
                })
                .sum();
    }
}
