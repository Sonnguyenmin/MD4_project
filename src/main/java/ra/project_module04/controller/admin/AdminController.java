package ra.project_module04.controller.admin;

import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<?> admin() {
        return ResponseEntity.ok().body("Chào mừng đến với trang quản trị");
    }

    @GetMapping("/userAdmin")
    public ResponseEntity<DataResponse> getAllUserAdmin() {
        return new ResponseEntity<>(new DataResponse(userService.getAllUsers(), HttpStatus.OK), HttpStatus.OK);
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
    public ResponseEntity<Users> changeUserStatus(@PathVariable Long id, @RequestParam Boolean status) throws CustomException {
        Users changeUserStatus = userService.updateUserStatus(id, status);
        return new ResponseEntity<>(changeUserStatus, HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Roles>> getAll(){
        return new ResponseEntity<>(roleService.getAllRoles(), HttpStatus.OK);
    }

}
