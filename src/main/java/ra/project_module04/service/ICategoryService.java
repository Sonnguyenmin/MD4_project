package ra.project_module04.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.project_module04.exception.CustomException;
import ra.project_module04.model.dto.req.CategoryRequest;
import ra.project_module04.model.entity.Category;

import java.util.List;

public interface ICategoryService {
    List<Category> getAllCategories();
    Category getCategoryById(Long id);
    Category addCategory(CategoryRequest category) throws CustomException;
    Category updateCategory(CategoryRequest category, Long id) throws CustomException;
    void deleteCategory(Long id) throws CustomException;
    Page<Category> getCategoryWithPaginationAndSorting(Integer page, Integer pageSize, String sortBy, String orderBy, String searchName);
    Page<Category> listCategoriesForSale(Pageable pageable);
}
