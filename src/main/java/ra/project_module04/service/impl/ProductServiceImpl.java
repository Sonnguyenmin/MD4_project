package ra.project_module04.service.impl;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ra.project_module04.exception.CustomException;
import ra.project_module04.model.dto.req.ProductRequest;
import ra.project_module04.model.entity.Category;
import ra.project_module04.model.entity.Product;
import ra.project_module04.repository.IProductRepository;
import ra.project_module04.service.ICategoryService;
import ra.project_module04.service.IProductService;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {
    private final IProductRepository productRepository;

    private final ICategoryService categoryService;

    private final UploadFile uploadFile;

    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Không tồn tại sản phẩm nào có mã: "+id));
    }

    @Override
    public Product addProduct(ProductRequest product) throws CustomException {
        if (productRepository.existsByProductName(product.getProductName())) {
            throw new CustomException("Tên sản phẩm đã tồn tại", HttpStatus.CONFLICT);
        }
        Product prod = Product.builder()
                .productName(product.getProductName())
                .description(product.getDescription())
                .unitPrice(product.getUnitPrice())
                .stockQuantity(product.getStockQuantity())
                .image(uploadFile.uploadLocal(product.getImage()))
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        Category category = categoryService.getCategoryById(product.getCategoryId());
        prod.setCategory(category);
        return productRepository.save(prod);
    }

    @Override
    public Product updateProduct(ProductRequest product, Long id) throws CustomException {
        productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Không tồn tại sản phẩm có mã: " + id));
        if(productRepository.existsByProductName(product.getProductName())) {
            throw new CustomException("Tên sản phẩm đã tồn tại", HttpStatus.CONFLICT);
        }

        Product prod = Product.builder()
                .productName(product.getProductName())
                .description(product.getDescription())
                .unitPrice(product.getUnitPrice())
                .stockQuantity(product.getStockQuantity())
                .image(uploadFile.uploadLocal(product.getImage()))
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        Category category = categoryService.getCategoryById(product.getCategoryId());
        prod.setCategory(category);
        prod.setId(id);
        return productRepository.save(prod);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Không tồn tại sản phẩm: " + id));
        productRepository.deleteById(id);
    }

    @Override
    public Page<Product> getProductWithPaginationAndSorting(Integer page, Integer pageSize, String sortBy, String orderBy, String searchName) {
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
        if (searchName.isEmpty()) {
            return productRepository.findAll(pageable);
        } else {
            return productRepository.findAllByProductNameContains(searchName, pageable);
        }
    }
}
