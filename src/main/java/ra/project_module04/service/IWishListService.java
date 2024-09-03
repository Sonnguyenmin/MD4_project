package ra.project_module04.service;

import ra.project_module04.exception.CustomException;
import ra.project_module04.model.dto.req.WishListRequest;
import ra.project_module04.model.dto.resp.WishListResponse;

import java.util.List;

public interface IWishListService {
    WishListResponse addWishList(WishListRequest wishListRequest);
    List<WishListResponse> getAllWishList() throws CustomException;
    void deleteWishList(Long id) throws CustomException;
}
