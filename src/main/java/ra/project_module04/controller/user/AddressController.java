package ra.project_module04.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.project_module04.exception.CustomException;
import ra.project_module04.model.dto.req.AddressRequest;
import ra.project_module04.model.dto.resp.AddressResponse;
import ra.project_module04.model.dto.resp.DataResponse;
import ra.project_module04.service.IAddressService;

import java.util.List;

@RestController
@RequestMapping("/api.example.com/v1/user")
@RequiredArgsConstructor
public class AddressController {
    private final IAddressService addressService;

    @PostMapping("/addAddress")
    public ResponseEntity<DataResponse> addAddress(@Valid @RequestBody AddressRequest address) {
        addressService.addNewAddress(address);
        return new ResponseEntity<>(new DataResponse("Thêm địa chỉ người dùng thành công", HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping("/address")
    public ResponseEntity<DataResponse> getAllAddresses() throws CustomException {
            List<AddressResponse> addressResponse = addressService.getUserAddresses();
            return new ResponseEntity<>(new DataResponse(addressResponse, HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping("/address/{id}")
    public ResponseEntity<DataResponse> getAddressById(@PathVariable Long id) {
        AddressResponse addressResponse = addressService.getAddressById(id);
        return new ResponseEntity<>(new DataResponse(addressResponse, HttpStatus.OK), HttpStatus.OK);
    }

    @DeleteMapping("/address/{id}")
    public ResponseEntity<DataResponse> deleteAddress(@PathVariable Long id) throws CustomException {
        addressService.deleteAddressById(id);
        return new ResponseEntity<>(new DataResponse("Đã xóa thành công địa chỉ", HttpStatus.OK), HttpStatus.OK);
    }
}
