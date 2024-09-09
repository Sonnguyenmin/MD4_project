package ra.project_module04.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.project_module04.exception.CustomException;
import ra.project_module04.model.dto.resp.DataResponse;
import ra.project_module04.model.entity.Roles;
import ra.project_module04.model.entity.Users;
import ra.project_module04.service.IRoleService;
import ra.project_module04.service.IUserService;

import java.util.List;


@RestController
@RequestMapping("/api.example.com/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final IUserService userService;

    private final IRoleService roleService;

    @GetMapping
    public ResponseEntity<DataResponse> admin() {
        return new ResponseEntity<>(new DataResponse("Chào mừng đến với trang quản trị", HttpStatus.OK), HttpStatus.OK);
    }


    @GetMapping("/userAdmin")
    public ResponseEntity<DataResponse> getAllUserAdmin(@PageableDefault(page = 0,size = 5, sort = "id",direction = Sort.Direction.ASC) Pageable pageable, @RequestParam(defaultValue = "" ) String search) {
        return new ResponseEntity<>(new DataResponse(userService.getAllUsers(pageable, search), HttpStatus.OK), HttpStatus.OK);
    }


    @GetMapping("/searchByName")
    public ResponseEntity<DataResponse> searchUserByName(@RequestParam(name = "searchName", defaultValue = "")String searchName,
                                                            @RequestParam(name = "page", defaultValue = "0")Integer page,
                                                            @RequestParam(name = "pageSize", defaultValue = "2")Integer pageSize,
                                                            @RequestParam(name = "sortBy", defaultValue = "")String sortBy,
                                                            @RequestParam(name = "orderBy", defaultValue = "asc")String orderBy){
        return new ResponseEntity<>(new DataResponse(userService.getUsersWithPaginationAndSorting(page, pageSize, sortBy, orderBy, searchName).getContent(),HttpStatus.OK),HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DataResponse> changeUserStatus(@PathVariable Long id, @RequestParam Boolean status) throws CustomException {
        Users changeUserStatus = userService.updateUserStatus(id, status);
        return new ResponseEntity<>(new DataResponse(changeUserStatus, HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<DataResponse> getAll(){
        return new ResponseEntity<>(new DataResponse(roleService.getAllRoles(), HttpStatus.OK), HttpStatus.OK);
    }
}
