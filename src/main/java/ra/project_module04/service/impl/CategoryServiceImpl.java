package ra.project_module04.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ra.project_module04.exception.CustomException;
import ra.project_module04.model.dto.req.CategoryRequest;
import ra.project_module04.model.entity.Category;
import ra.project_module04.repository.ICategoryRepository;
import ra.project_module04.repository.IProductRepository;
import ra.project_module04.service.ICategoryService;


import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements ICategoryService {

    private final ICategoryRepository categoryRepository;

    private final IProductRepository productRepository;


    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();

        if (categories.isEmpty()) {
            throw new NoSuchElementException("Không có danh mục nào.");
        }

        return categories;
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Không tồn tại danh mục với id: " + id));

    }

    @Override
    public Category addCategory(CategoryRequest category) throws CustomException {
        if (categoryRepository.existsByCategoryName(category.getCategoryName())) {
            throw new CustomException("Tên danh mục đã tồn tại", HttpStatus.BAD_REQUEST);
        }

        Category cate = Category.builder()
                .categoryName(category.getCategoryName())
                .description(category.getDescription())
                .status(category.getStatus())
                .build();
        return categoryRepository.save(cate);
    }

    @Override
    public Category updateCategory(CategoryRequest category, Long id) throws CustomException {
        Category existingCategory = categoryRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Khong ton tai danh muc nao co ma: " + id));

        if (!existingCategory.getCategoryName().equals(category.getCategoryName())
                && categoryRepository.existsByCategoryName(category.getCategoryName())) {
            throw new CustomException("Tên danh mục đã tồn tại", HttpStatus.BAD_REQUEST);
        }

        Category cate = Category.builder()
                .categoryName(category.getCategoryName())
                .description(category.getDescription())
                .status(category.getStatus())
                .build();
        cate.setId(id);
        return categoryRepository.save(cate);
    }

    @Override
    public void deleteCategory(Long id){
        categoryRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Khong ton tai danh mục: " + id));

        if(productRepository.existsByCategory_Id(id)) {
            throw new NoSuchElementException("Không thể xóa danh mục vì danh mục có sản phẩm");
        }

        categoryRepository.deleteById(id);
    }

    @Override
    public Page<Category> getCategoryWithPaginationAndSorting(Integer page, Integer pageSize, String sortBy, String orderBy, String searchName) {
        Pageable pageable;
        // Xác định cách sắp xếp
        if (!sortBy.isEmpty()) {
            Sort sort;
            switch (sortBy) {
                case "asc":
                    sort = Sort.by(orderBy).ascending();
                    break;
                case "desc":
                    sort = Sort.by(orderBy).descending();
                    break;
                default:
                    sort = Sort.by(orderBy).ascending();
            }
            pageable = PageRequest.of(page, pageSize, sort);
        } else {
            pageable = PageRequest.of(page, pageSize);
        }

        // Tìm danh mục
        Page<Category> categoriesPage;
        if (searchName.isEmpty()) {
            categoriesPage = categoryRepository.findAll(pageable);
        } else {
            categoriesPage = categoryRepository.findAllByCategoryNameContains(searchName, pageable);
        }

        // Kiểm tra nếu không có danh mục
        if (categoriesPage.isEmpty()) {
            throw new NoSuchElementException("Không tìm thấy danh mục");
        }

        return categoriesPage;
    }

    @Override
    public Page<Category> listCategoriesForSale(Pageable pageable) {
        return categoryRepository.findCategoriesByStatusTrue(pageable);
    }
}
