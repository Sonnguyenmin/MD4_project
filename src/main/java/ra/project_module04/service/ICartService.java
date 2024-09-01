package ra.project_module04.service;

import ra.project_module04.model.dto.req.AddToCartRequest;
import ra.project_module04.model.dto.resp.CartResponse;
import ra.project_module04.model.entity.ShoppingCart;

import java.util.List;

public interface ICartService {
    ShoppingCart addToCart(AddToCartRequest addToCart);
    List<CartResponse> getCart() ;
    void removeProductToCart(Long id);
    void removeAllProductToCart();

    ShoppingCart updateCartQuantity(Long id, Integer quantity);

}
