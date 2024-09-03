package ra.project_module04.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ra.project_module04.advice.SuccessException;
import ra.project_module04.exception.CustomException;
import ra.project_module04.model.dto.req.AddressRequest;
import ra.project_module04.model.dto.resp.AddressResponse;
import ra.project_module04.model.entity.Address;
import ra.project_module04.model.entity.Users;
import ra.project_module04.repository.IAddressRepository;
import ra.project_module04.service.IAddressService;
import ra.project_module04.service.IUserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements IAddressService {

    private final IAddressRepository addressRepository;

    private final IUserService userService;

    @Override
    public Address addNewAddress(AddressRequest address) {
        Users user = userService.getCurrentLoggedInUser();
        if (address.getPhone() != null) {
            if (addressRepository.existsByPhone(address.getPhone())) {
                throw new NoSuchElementException("Số điện thoại đã tồn tại");
            }
        }
        Address newAddress = Address.builder()
                .users(user)
                .fullAddress(address.getFullAddress())
                .phone(address.getPhone())
                .receiveName(address.getReceiveName())
                .build();
        return addressRepository.save(newAddress);
    }

    @Override
    public List<AddressResponse> getUserAddresses() throws CustomException {
        Users user = userService.getCurrentLoggedInUser();

        List<Address> addresses = addressRepository.findByUsers(user);
        if (addresses.isEmpty()) {
            throw new CustomException("Không có địa chỉ nào của người dùng", HttpStatus.NOT_FOUND);
        }

        List<AddressResponse> responseList = addresses.stream()
                .map( addr -> {
                    AddressResponse addressResponse = new AddressResponse();
                    addressResponse.setId(addr.getId());
                    addressResponse.setFullAddress(addr.getFullAddress());
                    addressResponse.setPhone(addr.getPhone());
                    addressResponse.setReceiveName(addr.getReceiveName());
                    return addressResponse;
                        }).collect(Collectors.toList());
        return responseList;
    }

    @Override
    public AddressResponse getAddressById(Long id) {
        Users user = userService.getCurrentLoggedInUser();
        Address address = addressRepository.findByIdAndUsers(id, user)
                .orElseThrow(() -> new NoSuchElementException("Không tồn tại địa ch có id: "+id));
        return AddressResponse.builder()
                .id(address.getId())
                .fullAddress(address.getFullAddress())
                .phone(address.getPhone())
                .receiveName(address.getReceiveName())
                .build();
    }

    @Override
    public void deleteAddressById(Long id) throws CustomException {
        Users user = userService.getCurrentLoggedInUser();
        Address address = addressRepository.findByIdAndUsers(id, user).orElseThrow(()-> new CustomException("Không tồn tại địa chỉ này", HttpStatus.NOT_FOUND));
        if(address.getUsers().getId().equals(user.getId())) {
            addressRepository.delete(address);
            throw new SuccessException("Đã xóa thành công địa chỉ");
        }
        else {
            throw new CustomException("Không tồn tại địa chỉ của bạn", HttpStatus.NOT_FOUND);
        }
    }

}
