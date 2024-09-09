package ra.project_module04.service.impl;

import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ra.project_module04.advice.SuccessException;
import ra.project_module04.constants.OrderStatus;
import ra.project_module04.exception.CustomException;
import ra.project_module04.model.dto.req.OrderRequest;
import ra.project_module04.model.dto.resp.OrderDetailResponse;
import ra.project_module04.model.dto.resp.OrderResponse;
import ra.project_module04.model.dto.resp.TopSellingProductResponse;
import ra.project_module04.model.entity.*;
import ra.project_module04.repository.*;
import ra.project_module04.service.IAddressService;
import ra.project_module04.service.IOrderService;
import ra.project_module04.service.IUserService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {
    private final IOrderRepository orderRepository;

    private final ICartRepository cartRepository;

    private final IProductRepository productRepository;

    private final IUserService userService;

    private final IOrderDetailRepository orderDetailRepository;

    private final IAddressService addressService;

    private final IAddressRepository addressRepository;



    @Override
    public Order orderNow(OrderRequest orderRequest) {
        Users user = userService.getCurrentLoggedInUser();

        List<ShoppingCart> shoppingCartsItems = cartRepository.findAllByUsers(user);
        if(shoppingCartsItems.isEmpty()) {
            throw new NoSuchElementException("Giỏ hàng trống, không có sản phẩm nào để đặt hàng");
        }

        // Tìm địa chỉ theo ID
//        Address address = addressService.findByIdAndUser(id, user);
//        if (address == null) {
//            throw new NoSuchElementException("Không tìm thấy địa chỉ cho người dùng. Vui lòng thêm địa chỉ trước khi đặt hàng.");
//        }

        Address address = addressService.getDefaultAddressForUser(user);

        if (address == null) {
            throw new NoSuchElementException("Không tìm thấy địa chỉ cho người dùng. Vui lòng thêm địa chỉ trước khi đặt hàng.");
        }
        Order order = Order.builder()
                .users(user)
                .serialNumber(UUID.randomUUID().toString())
                .totalPrice(calculateTotalPrice(shoppingCartsItems))
                .status(OrderStatus.WAITING)
                .receiveAddress(address.getFullAddress())
                .receivePhone(address.getPhone())
                .receiveName(address.getReceiveName())
                .note(orderRequest.getNote())
                .createdAt(new Date())
                .receivedAt(addDays(new Date(), 4))
                .build();
        order = orderRepository.save(order);

        //Luu danh sách chi tiết đơn hàng
        Order findOder = order;
        List<OrderDetails> list = shoppingCartsItems.stream().map(
                item->{ Product product = item.getProduct();
        if(item.getOrderQuantity() > product.getStockQuantity()) {
            throw new NoSuchElementException("Số lượng đặt hàng vượt quá số lượng sản phẩm");
        }
        product.setStockQuantity(product.getStockQuantity() - item.getOrderQuantity());
        productRepository.save(product);
                    return OrderDetails.builder()
                            .order(findOder)
                            .product(product)
                            .name(product.getProductName())
                            .unitPrice(product.getUnitPrice())
                            .orderQuantity(item.getOrderQuantity())
                            .build();
                }).collect(Collectors.toList());
        List<OrderDetails> orderDetails = orderDetailRepository.saveAll(list);
        order.setOrderDetails(orderDetails);
        cartRepository.deleteAll(shoppingCartsItems);
        return order;
    }

    private Double calculateTotalPrice(List<ShoppingCart> shoppingCartItems) {
        return shoppingCartItems.stream().mapToDouble(item-> {
                    Product product = item.getProduct();
                    return product.getUnitPrice() * item.getOrderQuantity();
                })
                .sum();
    }

    private Date addDays(Date date, Integer days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }

    @Override
    public List<Order> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        if(orders.isEmpty()) {
            throw new SuccessException("Không có sản phẩm nào trong đơn hàng");
        }

        return orders;
    }

    @Override
    public List<OrderResponse> getOrderResponsesByStatus(OrderStatus status) {
        List<Order> orders = orderRepository.findByStatus(status);

        if(orders.isEmpty()) {
            throw new NoSuchElementException("Không có đơn hàng nào trong trạng thái: " + status);
        }

        return orders.stream().map(order -> OrderResponse.builder()
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
                .orderDetail(order.getOrderDetails().stream().map(detail ->
                                OrderDetailResponse.builder()
                                        .productId(detail.getProduct().getId())
                                        .name(detail.getName())
                                        .unitPrice(detail.getUnitPrice())
                                        .quantity(detail.getOrderQuantity())
                                        .build())
                        .collect(Collectors.toList()))
                .build()).collect(Collectors.toList());
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        Order order =  orderRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Không tồn tại đơn hàng"));

        return OrderResponse.builder()
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
                .orderDetail(
                        order.getOrderDetails().stream().map(detail ->
                                        OrderDetailResponse.builder()
                                                .productId(detail.getProduct().getId())
                                                .name(detail.getName())
                                                .unitPrice(detail.getUnitPrice())
                                                .quantity(detail.getOrderQuantity())
                                                .build())
                                .collect(Collectors.toList()))
                .build();
    }

    @Override
    public boolean updateOrderStatus(Long id, OrderStatus status) throws CustomException {
        Order order = orderRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Không tồn tại đơn hàng"));

        OrderStatus currentStatus = order.getStatus();

        if(order.getStatus() == OrderStatus.CANCEL) {
            throw new CustomException("Không thể thay đổi trạng thái đơn hàng vì người dùng đã hủy đơn hàng", HttpStatus.BAD_REQUEST);
        }

        if(order.getStatus() == OrderStatus.SUCCESS) {
            throw new CustomException("Đơn hàng đã giao thành công nên khôn thể thay đổi trạng thái", HttpStatus.BAD_REQUEST);
        }

        if(!canChaneStatus(currentStatus, status)){
            throw new CustomException("Không thể thay dổi trang thái của đơn hàng trước đó", HttpStatus.BAD_REQUEST);
        }
        order.setStatus(status);
        orderRepository.save(order);
        return true;
    }

    private boolean canChaneStatus(OrderStatus currentStatus, OrderStatus newStatus) {
        return currentStatus.ordinal() < newStatus.ordinal();
    }


    @Override
    public List<Order> getAllUserOrders() {
        Users user = userService.getCurrentLoggedInUser();
        List<Order> orders = orderRepository.findAllByUsers(user);
        if (orders.isEmpty()) {
            throw new IllegalArgumentException("Không có đơn hàng nào cho người dùng này.");
        }
        return orders;
    }

    @Override
    public Order getOrderBySerialNumber(String serialNumber) {
        Users user = userService.getCurrentLoggedInUser();
        return orderRepository.findBySerialNumberAndUsers(serialNumber, user)
                .orElseThrow(() ->new NoSuchElementException("Không tồn tại đơn hàng với mã này"));
    }

    @Override
    public List<Order> getOrdersByStatus(OrderStatus orderStatus) {
        Users user = userService.getCurrentLoggedInUser();
        List<Order> orders = orderRepository.findByStatusAndUsers(orderStatus, user);
        if (orders.isEmpty()) {
            throw new NoSuchElementException("Không tìm thấy đơn hàng nào với trạng thái: " + orderStatus);
        }
        return orders;
    }


    @Override
    public boolean cancelOrder(Long id) {
        // Lấy người dùng hiện tại
        Users user = userService.getCurrentLoggedInUser();

        // Tìm đơn hàng theo ID và người dùng
        Order order = orderRepository.findByIdAndUsers(id, user)
                .orElseThrow(() -> new NoSuchElementException("Không tồn tại đơn hàng hoặc bạn không có quyền truy cập"));

        // Kiểm tra trạng thái đơn hàng
        if (order.getStatus() == OrderStatus.WAITING) {
            // Cập nhật số lượng sản phẩm trong kho
            for (OrderDetails orderDetail : order.getOrderDetails()) {
                Product product = orderDetail.getProduct();
                product.setStockQuantity(product.getStockQuantity() + orderDetail.getOrderQuantity());
                productRepository.save(product);
            }

            // Thay đổi trạng thái đơn hàng thành CANCEL
            order.setStatus(OrderStatus.CANCEL);
            orderRepository.save(order);
            return true;
        } else if (order.getStatus() == OrderStatus.CONFIRM) {
            throw new IllegalArgumentException("Sản phẩm đã được xác nhận, bạn không thể hủy đơn hàng");
        }

        return false; // Trả về false nếu không có điều kiện nào được thỏa mãn
    }

    @Override
    public List<TopSellingProductResponse> getTopSellingProducts(Integer limit) throws CustomException {
        Pageable pageable = PageRequest.of(0, limit);
        List<Object[]> results = orderDetailRepository.findTopSellingProducts(pageable);

        if (results.isEmpty()) {
            throw new CustomException("Không có sản phẩm bán chạy nào", HttpStatus.BAD_REQUEST);
        }

        List<TopSellingProductResponse> topSellingProducts = new ArrayList<>();
        for (Object[] result : results) {
            Product product = (Product) result[0];
            Long purchaseCount = (Long) result[1];

            TopSellingProductResponse response = new TopSellingProductResponse();
            response.setProduct(product);
            response.setPurchaseCount(purchaseCount);

            topSellingProducts.add(response);
        }
        return topSellingProducts;
    }


}

