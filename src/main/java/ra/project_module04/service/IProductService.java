package ra.project_module04.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.project_module04.exception.CustomException;
import ra.project_module04.model.dto.req.ProductRequest;
import ra.project_module04.model.entity.Product;

import java.util.List;

public interface IProductService {
    Page<Product> getAllProduct(Pageable pageable, String search);
    Product getProductById(Long id);
    Product addProduct(ProductRequest product) throws CustomException;
    Product updateProduct(ProductRequest product, Long id) throws CustomException;
    void deleteProduct(Long id) throws CustomException;
    Page<Product> getProductWithPaginationAndSorting(Integer page, Integer pageSize, String sortBy, String orderBy, String searchName) throws CustomException;
    List<Product> findProductByCategoryId(Long id);
    List<Product> getLatestProduct();
    List<Product> findProductByProductNameOrDescription(String search);

    Page<Product> listProductsForSale(Pageable pageable);

    List<Product> getProductsSortedByPrice();
}
