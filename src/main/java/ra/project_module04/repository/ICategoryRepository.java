package ra.project_module04.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ra.project_module04.model.entity.Category;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {
    //@Query("select c from Category c where c.categoryName like concat('%',:categoryName,'%')")
    Page<Category> findAllByCategoryNameContains(String categoryName, Pageable pageable);
    boolean existsByCategoryName(String categoryName);
    boolean existsById(Long id);
    Page<Category> findCategoriesByStatusTrue(Pageable pageable);
}
