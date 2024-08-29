package ra.project_module04.service;

import org.springframework.data.domain.Page;
import ra.project_module04.exception.CustomException;
import ra.project_module04.model.dto.req.ProductRequest;
import ra.project_module04.model.entity.Product;

import java.util.List;

public interface IProductService {
    List<Product> getAllProduct();
    Product getProductById(Long id);
    Product addProduct(ProductRequest product) throws CustomException;
    Product updateProduct(ProductRequest product, Long id) throws CustomException;
    void deleteProduct(Long id);
    Page<Product> getProductWithPaginationAndSorting(Integer page, Integer pageSize, String sortBy, String orderBy, String searchName);
}
