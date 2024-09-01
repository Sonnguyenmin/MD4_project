package ra.project_module04.service;

import ra.project_module04.model.dto.req.OrderRequest;
import ra.project_module04.model.entity.Order;

public interface IOrderService {
    Order orderNow(OrderRequest orderRequest);
}
