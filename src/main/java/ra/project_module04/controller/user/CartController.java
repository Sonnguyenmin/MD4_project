package ra.project_module04.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.project_module04.model.dto.req.AddToCartRequest;
import ra.project_module04.model.dto.resp.DataResponse;
import ra.project_module04.model.entity.ShoppingCart;
import ra.project_module04.service.ICartService;
import ra.project_module04.service.IUserService;

@RestController
@RequestMapping("/api.example.com/v1/user")
@RequiredArgsConstructor
public class CartController {
    private final IUserService userService;

    private final ICartService cartService;

    @PostMapping("/cart/add")
    public ResponseEntity<DataResponse> addToCart(@RequestBody AddToCartRequest addToCart){
        return new ResponseEntity<>(new DataResponse(cartService.addToCart(addToCart), HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping("cart/list")
    public ResponseEntity<DataResponse> getCartList(){
        return new ResponseEntity<>(new DataResponse(cartService.getCart(), HttpStatus.OK), HttpStatus.OK);
    }

    @DeleteMapping("/cart/{id}")
    public ResponseEntity<DataResponse> removeFromCart(@PathVariable Long id){
        cartService.removeProductToCart(id);
        return new ResponseEntity<>(new DataResponse("Đã xóa thành công sản phẩm trong giỏ hàng có ID : "+id, HttpStatus.OK), HttpStatus.OK);
    }

    @DeleteMapping("/cart/deleteAllProduct")
    public ResponseEntity<DataResponse> deleteAllProduct(){
        cartService.removeAllProductToCart();
        return new ResponseEntity<>(new DataResponse("Đã xóa thành công tất cả sản phẩm trong giỏ hàng", HttpStatus.OK), HttpStatus.OK);
    }

    @PutMapping("/cart/cartUpdateQuantity/{id}")
    public ResponseEntity<DataResponse> updateCartFromQuantity(@PathVariable Long id, @RequestParam Integer quantity) {
        ShoppingCart shoppingCart = cartService.updateCartQuantity(id, quantity);
        return new ResponseEntity<>(new DataResponse(shoppingCart, HttpStatus.OK), HttpStatus.OK);
    }


}
