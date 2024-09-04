package ra.project_module04.controller.user;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.project_module04.exception.CustomException;
import ra.project_module04.model.dto.req.WishListRequest;
import ra.project_module04.model.dto.resp.DataResponse;
import ra.project_module04.model.dto.resp.WishListResponse;
import ra.project_module04.service.IWishListService;

import java.util.List;

@RestController
@RequestMapping("/api.example.com/v1/user")
@RequiredArgsConstructor
public class WishListController {

    private final IWishListService wishListService;

    @PostMapping("/wishList")
    public ResponseEntity<WishListResponse> addWishList(@RequestBody WishListRequest wishListRequest) throws CustomException {
        WishListResponse wishListResponse = wishListService.addWishList(wishListRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(wishListResponse);
    }

    @GetMapping("/wishList")
    public ResponseEntity<List<WishListResponse>> getWishList() throws CustomException {
        List<WishListResponse> wishListResponses = wishListService.getAllWishList();
        return ResponseEntity.status(HttpStatus.OK).body(wishListResponses);
    }

    @DeleteMapping("/wishList/{id}")
    public ResponseEntity<DataResponse> deleteWishList(@PathVariable Long id) throws CustomException {
         wishListService.deleteWishList(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
