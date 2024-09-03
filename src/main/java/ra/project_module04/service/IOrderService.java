package ra.project_module04.service;

import ra.project_module04.constants.OrderStatus;
import ra.project_module04.model.dto.req.OrderRequest;
import ra.project_module04.model.entity.Order;

import java.util.List;

public interface IOrderService {
    Order orderNow(OrderRequest orderRequest);
    List<Order> getAllOrders();
    List<Order> getOrdersByStatus(OrderStatus status);
}
