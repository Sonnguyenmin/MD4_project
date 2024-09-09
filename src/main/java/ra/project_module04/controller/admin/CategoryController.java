package ra.project_module04.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.project_module04.exception.CustomException;
import ra.project_module04.model.dto.req.CategoryRequest;
import ra.project_module04.model.dto.resp.DataResponse;
import ra.project_module04.service.impl.CategoryServiceImpl;


@RestController
@RequestMapping("/api.example.com/v1/admin/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryServiceImpl categoryService;

    @GetMapping
    public ResponseEntity<DataResponse> getAllCategories( ) {
        return new ResponseEntity<>(new DataResponse(categoryService.getAllCategories(), HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponse> getCategoryById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(new DataResponse(categoryService.getCategoryById(id), HttpStatus.OK), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DataResponse> addCategory(@Valid @RequestBody CategoryRequest category) throws CustomException {
        return new ResponseEntity<>(new DataResponse(categoryService.addCategory(category), HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DataResponse> updateCategory(@Valid @RequestBody CategoryRequest category, @PathVariable("id") Long id) throws CustomException {
        return new ResponseEntity<>(new DataResponse(categoryService.updateCategory(category, id), HttpStatus.OK), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DataResponse> deleteCategory(@PathVariable("id") Long id) throws CustomException {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(new DataResponse("Đã xóa thành công danh mục có mã: "+id, HttpStatus.OK), HttpStatus.OK);
    }


    @GetMapping("/searchByCategoryName")
    public ResponseEntity<DataResponse> searchByCategoryName(@RequestParam(name = "searchName", defaultValue = "")String searchName,
                                                             @RequestParam(name = "page", defaultValue = "0")Integer page,
                                                             @RequestParam(name = "pageSize", defaultValue = "2")Integer pageSize,
                                                             @RequestParam(name = "sortBy", defaultValue = "")String sortBy,
                                                             @RequestParam(name = "orderBy", defaultValue = "asc")String orderBy) {

        return new ResponseEntity<>(new DataResponse(categoryService.getCategoryWithPaginationAndSorting(page, pageSize, sortBy, orderBy, searchName).getContent(), HttpStatus.OK), HttpStatus.OK);
    }

}
