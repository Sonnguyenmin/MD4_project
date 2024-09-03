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
        return new ResponseEntity<>(new DataResponse(addressService.addNewAddress(address), HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping("/address")
    public ResponseEntity<List<AddressResponse>> getAllAddresses() throws CustomException {
            List<AddressResponse> addressResponse = addressService.getUserAddresses();
            return ResponseEntity.status(HttpStatus.OK).body(addressResponse);
    }

    @GetMapping("/address/{id}")
    public ResponseEntity<AddressResponse> getAddressById(@PathVariable Long id) throws CustomException {
        AddressResponse addressResponse = addressService.getAddressById(id);
        return ResponseEntity.status(HttpStatus.OK).body(addressResponse);
    }

    @DeleteMapping("/address/{id}")
    public ResponseEntity<DataResponse> deleteAddress(@PathVariable Long id) throws CustomException {
        addressService.deleteAddressById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
