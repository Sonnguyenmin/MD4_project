package ra.project_module04.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ra.project_module04.model.dto.req.AddToCartRequest;
import ra.project_module04.model.dto.resp.CartResponse;
import ra.project_module04.model.entity.Product;
import ra.project_module04.model.entity.ShoppingCart;
import ra.project_module04.model.entity.Users;
import ra.project_module04.repository.ICartRepository;
import ra.project_module04.repository.IProductRepository;
import ra.project_module04.service.ICartService;
import ra.project_module04.service.IUserService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements ICartService {
    private final ICartRepository cartRepository;

    private final IUserService userService;

    private final IProductRepository productRepository;


    @Override
    public ShoppingCart addToCart(AddToCartRequest addToCart) {

        if (addToCart.getProductId() == null) {
            throw new NoSuchElementException("Mã sản phẩm không được phép rỗng");
        }
        if (addToCart.getQuantity() == null || addToCart.getQuantity() <= 0) {
            throw new NoSuchElementException("Số lượng không để trống và số lượng phải lớn hơn 0");
        }

        Users user = userService.getCurrentLoggedInUser();
        Product product = productRepository.findById(addToCart.getProductId())
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy sản phẩm này"));

        if (addToCart.getQuantity() > product.getStockQuantity()) {
            throw new NoSuchElementException("Số lượng sản phẩm không đủ trong kho");
        }

        ShoppingCart shoppingCart = cartRepository.findByUsersAndProduct(user, product)
                .orElse(ShoppingCart.builder()
                        .users(user)
                        .product(product)
                        .orderQuantity(0)
                        .build());
        shoppingCart.setOrderQuantity(shoppingCart.getOrderQuantity() + addToCart.getQuantity());
        if (shoppingCart.getOrderQuantity() > product.getStockQuantity()) {
            throw new NoSuchElementException("Số lượng sản phẩm trong giỏ hàng vượt quá số lượng có sẵn trong kho");
        }
        return cartRepository.save(shoppingCart);
    }

    @Override
    public List<CartResponse> getCart() {
        Users user = userService.getCurrentLoggedInUser();
        List<ShoppingCart> shoppingCarts = cartRepository.findAllByUsers(user);
        return shoppingCarts.stream().map(cart -> CartResponse.builder()
                        .id(cart.getId())
                        .productId(cart.getId())
                        .productName(cart.getProduct().getProductName())
                        .orderQuantity(cart.getOrderQuantity())
                        .build())
                .collect(Collectors.toList());
    }


    @Override
    public void removeProductToCart(Long id) {
        Users user = userService.getCurrentLoggedInUser();
        ShoppingCart shoppingCart = cartRepository.findByIdAndUsers(id, user)
                .orElseThrow(() -> new NoSuchElementException("Không có sản phẩm trong giỏ hàng"));
        cartRepository.delete(shoppingCart);
    }


    @Override
    public void removeAllProductToCart() {
        Users user = userService.getCurrentLoggedInUser();
        List<ShoppingCart> shoppingCarts = cartRepository.findAllByUsers(user);
        cartRepository.deleteAll(shoppingCarts);
    }

    @Override
    public ShoppingCart updateCartQuantity(Long id, Integer quantity) {
        Users user = userService.getCurrentLoggedInUser();
        ShoppingCart shoppingCart = cartRepository.findByIdAndUsers(id, user)
                .orElseThrow(() -> new NoSuchElementException("Không có sản phẩm trong giỏ hàng"));

        Product product = productRepository.findById(shoppingCart.getProduct().getId())
                .orElseThrow(() -> new NoSuchElementException("Không tồn tại sản phẩm này"));

        if(quantity > product.getStockQuantity()){
            throw  new NoSuchElementException("Số lượng yêu cầu vượt quá số lượng sản phẩm");
        }

        shoppingCart.setOrderQuantity(quantity);
        return cartRepository.save(shoppingCart);
    }
}
