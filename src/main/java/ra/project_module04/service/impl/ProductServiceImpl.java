package ra.project_module04.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import ra.project_module04.exception.CustomException;
import ra.project_module04.model.dto.req.ProductRequest;
import ra.project_module04.model.entity.*;
import ra.project_module04.repository.*;
import ra.project_module04.service.ICategoryService;
import ra.project_module04.service.IProductService;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {
    private final IProductRepository productRepository;
    private final ICategoryRepository categoryRepository;

    private final ICategoryService categoryService;

    private final UploadFile uploadFile;

    private final IWishListRepository wishListRepository;

    private final IOrderDetailRepository orderDetailRepository;

    private final ICartRepository cartRepository;



    @Override
    public Page<Product> getAllProduct(Pageable pageable, String search) {
        Page<Product> products;
        if(search == null || search.isEmpty()) {
            products = productRepository.findAll(pageable);
        }else{
            products = productRepository.findProductByProductNameContainsIgnoreCase(search,pageable);
        }
        return products;
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Không tồn tại sản phẩm nào có mã: "+id));
    }

    @Override
    public Product addProduct(ProductRequest product) throws CustomException {
//        if (productRepository.existsByProductNameAndCategory_Id(product.getProductName(), product.getCategoryId())) {
//            throw new CustomException("Sản phẩm đã tồn tại trong danh mục", HttpStatus.CONFLICT);
//        }

        if (productRepository.existsByProductName(product.getProductName())) {
            throw new CustomException("Tên sản phẩm đã tồn tại", HttpStatus.BAD_REQUEST);
        }

        if(product.getUnitPrice() <= 0) {
            throw new CustomException("Giá sản phẩm phải lớn hơn 0", HttpStatus.BAD_REQUEST);
        }

        if(product.getStockQuantity() < 0) {
            throw new CustomException("Số lượng sản phẩm phải lớn hơn hoặc bằng 0", HttpStatus.BAD_REQUEST);
        }

        Category category = categoryService.getCategoryById(product.getCategoryId());
        if (!category.getStatus()) {
            throw new CustomException("Danh mục không hoạt động, không thể thêm sản phẩm", HttpStatus.BAD_REQUEST);
        }
        
        Product prod = Product.builder()
                .productName(product.getProductName())
                .description(product.getDescription())
                .unitPrice(product.getUnitPrice())
                .stockQuantity(product.getStockQuantity())
                .image(uploadFile.uploadLocal(product.getImage()))
                .status(product.getStatus())
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        prod.setCategory(category);
        return productRepository.save(prod);
    }

    @Override
    public Product updateProduct(ProductRequest product, Long id) throws CustomException {
        // Lấy sản phẩm hiện tại từ database
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Không tồn tại sản phẩm có mã: " + id));

        // Kiểm tra tên sản phẩm
        if (!existingProduct.getProductName().equals(product.getProductName())
                && productRepository.existsByProductName(product.getProductName())) {
            throw new CustomException("Tên sản phẩm đã tồn tại", HttpStatus.BAD_REQUEST);
        }

        // Kiểm tra giá sản phẩm
        if (product.getUnitPrice() <= 0) {
            throw new CustomException("Giá sản phẩm phải lớn hơn 0", HttpStatus.BAD_REQUEST);
        }

        // Kiểm tra số lượng sản phẩm
        if (product.getStockQuantity() < 0) {
            throw new CustomException("Số lượng sản phẩm phải lớn hơn hoặc bằng 0", HttpStatus.BAD_REQUEST);
        }

        // Cập nhật thông tin sản phẩm
        existingProduct.setProductName(product.getProductName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setUnitPrice(product.getUnitPrice());
        existingProduct.setStockQuantity(product.getStockQuantity());

        // Chỉ cập nhật ảnh nếu có ảnh mới
        if (product.getImage() != null && !product.getImage().isEmpty()) {
            existingProduct.setImage(uploadFile.uploadLocal(product.getImage()));
        }

        existingProduct.setStatus(product.getStatus());
        existingProduct.setUpdatedAt(new Date());

        // Cập nhật danh mục
        Category category = categoryService.getCategoryById(product.getCategoryId());
        existingProduct.setCategory(category);

        // Lưu sản phẩm đã cập nhật
        return productRepository.save(existingProduct);
    }

    @Override
    public void deleteProduct(Long id) throws CustomException {
       Product product = productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Không tồn tại sản phẩm: " + id));

        List<ShoppingCart> shoppingCarts = cartRepository.findByProduct(product);
        if (!shoppingCarts.isEmpty()) {
            throw new CustomException("Không thể xóa sản phẩm, Sản phẩm đã tồn tại trong giỏ hàng", HttpStatus.BAD_REQUEST);
        }

        List<OrderDetails> orderDetails = orderDetailRepository.findByProduct(product);
        if (!orderDetails.isEmpty()) {
            throw new CustomException("Không thể xóa sản phẩm, Sản phẩm đã tồn tại trong đơn hàng", HttpStatus.BAD_REQUEST);
        }

        List<WishList> wishList = wishListRepository.findByProduct(product);
        if(!wishList.isEmpty()) {
            throw new CustomException("Không thể xóa sản phẩm, Sản phẩm đã tồn tại trong sản phẩm yêu thích", HttpStatus.BAD_REQUEST);
        }

        productRepository.deleteById(id);
    }

    @Override
    public Page<Product> getProductWithPaginationAndSorting(Integer page, Integer pageSize, String sortBy, String orderBy, String searchName) throws CustomException {
        Pageable pageable;
        if (!sortBy.isEmpty()) {
            Sort sort;
            switch (sortBy) {
                case "asc":
                    sort = Sort.by(sortBy).ascending();
                    break;
                case "desc":
                    sort = Sort.by(sortBy).descending();
                    break;
                default:
                    sort = Sort.by(sortBy).ascending();
            }
            pageable = PageRequest.of(page, pageSize, sort);
        } else {
            pageable = PageRequest.of(page, pageSize);
        }

        Page<Product> products;

        if (searchName.isEmpty()) {
            products =  productRepository.findAll(pageable);
        } else {
            products =  productRepository.findAllByProductNameContains(searchName, pageable);
        }

        if(!products.hasContent()) {
            throw new IllegalArgumentException("Không tìm thấy sản phẩm bạn tìm kiếm !");
        }

        return products;
    }

    @Override
    public List<Product> findProductByProductNameOrDescription(String search) {
        List<Product> products = productRepository.findByProductNameOrDescriptionContaining(search);

        if (products.isEmpty()) {
            throw new NoSuchElementException("Không có sản phẩm nào được tìm thấy.");
        }
        return products;
    }

    @Override
    public List<Product> findProductByCategoryId(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new NoSuchElementException("Không tìm thấy danh mục với ID: " + id);
        }

        List<Product> products = productRepository.findProductsByCategory_Id(id);

        if (products.isEmpty()) {
            throw new NoSuchElementException("Danh mục với ID: " + id + " không có sản phẩm nào.");
        }
        return products;
    }

    @Override
    public List<Product> getLatestProduct() {
       // return productRepository.getLatestProducts(PageRequest.of(0,5));
        return  productRepository.findTop5ByOrderByCreatedAtAsc();
//        return productRepository.findTop5ByOrderByIdDesc();
    }


    @Override
    public Page<Product> listProductsForSale(Pageable pageable) {
        return productRepository.findProductByStatusTrue(pageable);
    }

    @Override
    public List<Product> getProductsSortedByPrice() {
        return productRepository.findAll(Sort.by(Sort.Direction.ASC, "unitPrice"));
    }
}
