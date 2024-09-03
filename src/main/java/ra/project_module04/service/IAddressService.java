package ra.project_module04.service;

import ra.project_module04.exception.CustomException;
import ra.project_module04.model.dto.req.AddressRequest;
import ra.project_module04.model.dto.resp.AddressResponse;
import ra.project_module04.model.entity.Address;

import java.util.List;

public interface IAddressService {
    Address addNewAddress(AddressRequest address);
    List<AddressResponse> getUserAddresses() throws CustomException;
    AddressResponse getAddressById(Long id);
    void deleteAddressById(Long id) throws CustomException;
}
