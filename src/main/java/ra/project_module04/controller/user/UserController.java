package ra.project_module04.controller.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.project_module04.exception.CustomException;
import ra.project_module04.model.dto.resp.DataResponse;
import ra.project_module04.model.entity.OrderDetails;
import ra.project_module04.model.entity.Product;
import ra.project_module04.service.*;

import java.util.List;

@RestController
@RequestMapping("/api.example.com/v1")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    private final ICategoryService categoryService;

    private final IProductService productService;

    private final IOrderService orderService;

    private final IWishListService wishListService;


    //Danh sách danh mục được bán
    @GetMapping("/category/categorySale")
    public ResponseEntity<DataResponse> getProductByCategoryForSale(@PageableDefault(page = 0,size = 4, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(new DataResponse(categoryService.listCategoriesForSale(pageable), HttpStatus.OK),HttpStatus.OK);
    }

    //Danh sách sản phẩm theo danh mục
    @GetMapping("/category/{id}")
    public ResponseEntity<DataResponse> getProductByCategory(@PathVariable("id") Long id) {
        return new ResponseEntity<>(new DataResponse(productService.findProductByCategoryId(id), HttpStatus.OK),HttpStatus.OK);
    }


    //Chi tiết thông tin sản phẩm theo id
    @GetMapping("/product/{id}")
    public ResponseEntity<DataResponse> getProductById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(new DataResponse(productService.getProductById(id), HttpStatus.OK), HttpStatus.OK);
    }

    //Danh sách sản phẩm mới nhất
    @GetMapping("/product/newProduct")
    public ResponseEntity<DataResponse> getNewProduct() {
        return new ResponseEntity<>(new DataResponse(productService.getLatestProduct(), HttpStatus.OK), HttpStatus.OK);
    }

    // Tìm kiếm sản phẩm theo tên hoặc mô tả
    @GetMapping("/product/search")
    public ResponseEntity<DataResponse> searchProduct(@RequestParam(defaultValue = "" )String search) {
        return new ResponseEntity<>(new DataResponse(productService.findProductByProductNameOrDescription(search), HttpStatus.OK),HttpStatus.OK);
    }


    //Danh sách sản phẩm được bán
    @GetMapping("/product/productSale")
    public ResponseEntity<DataResponse> getProductForSale(@PageableDefault(page = 0,size = 2, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return new ResponseEntity<>(new DataResponse(productService.listProductsForSale(pageable), HttpStatus.OK),HttpStatus.OK);
    }

    //Danh sách sản phẩm bán chạy
    @GetMapping("/top-selling-products")
    public ResponseEntity<List<Product>> getTopSellingProducts(@RequestParam(defaultValue = "5") Integer limit) throws CustomException {
        List<Product> topSellingProducts = orderService.getTopSellingProducts(limit);
        return new ResponseEntity<>(topSellingProducts, HttpStatus.OK);
    }

    //Danh sách sản phảm nổi bật
    @GetMapping("/top-outstanding-products")
    public ResponseEntity<List<Product>> getTopWishlistProducts(@RequestParam(defaultValue = "5") Integer limit) throws CustomException {
        List<Product> topWishlistProducts = wishListService.getTopWishlistProducts(limit);
        return new ResponseEntity<>(topWishlistProducts, HttpStatus.OK);
    }

}
