package ra.project_module04.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ra.project_module04.advice.SuccessException;
import ra.project_module04.exception.CustomException;
import ra.project_module04.model.dto.req.WishListRequest;
import ra.project_module04.model.dto.resp.WishListResponse;
import ra.project_module04.model.entity.Product;
import ra.project_module04.model.entity.Users;
import ra.project_module04.model.entity.WishList;
import ra.project_module04.repository.IProductRepository;
import ra.project_module04.repository.IWishListRepository;
import ra.project_module04.service.IUserService;
import ra.project_module04.service.IWishListService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class WishListServiceImpl implements IWishListService {

    private final IUserService userService;

    private final IProductRepository productRepository;

    private final IWishListRepository wishListRepository;

    @Override
    public WishListResponse addWishList(WishListRequest wishListRequest) {
        Users user = userService.getCurrentLoggedInUser();
        Product product = productRepository.findById(wishListRequest.getProductId())
                .orElseThrow(() -> new NoSuchElementException("Sản phẩm này không tồn tại"));

        WishList wishList = WishList.builder()
                .user(user)
                .product(product)
                .build();

        wishList = wishListRepository.save(wishList);

        // Chuyển đổi WishList thành WishListResponse
        WishListResponse response = new WishListResponse();
        response.setId(wishList.getId());
        response.setWishlistProName(product.getProductName());
        response.setProductId(product.getId());
        response.setUserId(user.getId());
        return response;
    }

    @Override
    public List<WishListResponse> getAllWishList() throws CustomException {
        Users user = userService.getCurrentLoggedInUser();
        List<WishList> wishList = wishListRepository.findAllByUser(user);
        if (wishList.isEmpty()) {
            throw new CustomException("Không có sản phẩm nào trong danh sách yêu thích", HttpStatus.NOT_FOUND);
        }
        List<WishListResponse> responseList = wishList.stream()
                .map(wish -> { WishListResponse response = new WishListResponse();
                    response.setId(wish.getId());
                    response.setProductId(wish.getProduct().getId());
                    response.setWishlistProName(wish.getProduct().getProductName());
                    response.setUserId(user.getId());
                    return response;}).collect(Collectors.toList());
        return responseList;
    }

    @Override
    public void deleteWishList(Long id) throws CustomException {
        Users user = userService.getCurrentLoggedInUser();
        WishList wishList = wishListRepository.findByIdAndUser(id, user).orElseThrow(()-> new CustomException("không tồn tại mã sản phẩm yêu thích này", HttpStatus.NOT_FOUND));
        if (wishList.getUser().getId().equals(user.getId())) {
            wishListRepository.delete(wishList);
            throw new SuccessException("Đã xóa thành công sản phẩm yêu thích");
        } else {
            throw new CustomException("Không tồn tại sản phẩm yêu thích của bạn", HttpStatus.NOT_FOUND);
        }
    }
}
