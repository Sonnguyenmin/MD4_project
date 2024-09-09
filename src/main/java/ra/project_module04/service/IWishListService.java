package ra.project_module04.service;

import ra.project_module04.exception.CustomException;
import ra.project_module04.model.dto.req.WishListRequest;
import ra.project_module04.model.dto.resp.WishListResponse;
import ra.project_module04.model.entity.Product;

import java.util.List;

public interface IWishListService {
    WishListResponse addWishList(WishListRequest wishListRequest) throws CustomException;
    List<WishListResponse> getAllWishList() throws CustomException;
    void deleteWishList(Long id) throws CustomException;
    List<Product> getTopWishlistProducts(Integer limit) throws CustomException;

}
