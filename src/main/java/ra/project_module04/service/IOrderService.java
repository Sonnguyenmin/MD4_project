package ra.project_module04.service;

import ra.project_module04.constants.OrderStatus;
import ra.project_module04.exception.CustomException;
import ra.project_module04.model.dto.req.OrderRequest;
import ra.project_module04.model.dto.resp.OrderResponse;
import ra.project_module04.model.entity.Order;

import java.util.List;

public interface IOrderService {
    Order orderNow(OrderRequest orderRequest);
    List<Order> getAllOrders();
    List<OrderResponse> getOrderResponsesByStatus(OrderStatus status) throws CustomException;
    OrderResponse getOrderById(Long id);
    boolean updateOrderStatus(Long id, OrderStatus status);
    List<Order> getAllUserOrders();
    Order getOrderBySerialNumber(String serialNumber);

    List<Order> getOrdersByStatus(OrderStatus orderStatus);

    boolean cancelOrder(Long id);
}
