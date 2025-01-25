package com.example.SpringAddressAPI.services.impl;

import com.example.SpringAddressAPI.dto.WebResponse;
import com.example.SpringAddressAPI.dto.address.AddressResponse;
import com.example.SpringAddressAPI.dto.user.UserListRequest;
import com.example.SpringAddressAPI.dto.user.UserRequest;
import com.example.SpringAddressAPI.dto.user.UserResponse;
import com.example.SpringAddressAPI.entity.Address;
import com.example.SpringAddressAPI.entity.Users;
import com.example.SpringAddressAPI.repository.AddressRepository;
import com.example.SpringAddressAPI.repository.UserRepository;
import com.example.SpringAddressAPI.services.UserServices;
import com.example.SpringAddressAPI.services.ValidatorServices;
import com.example.SpringAddressAPI.utils.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServicesImpl implements UserServices {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ValidatorServices validatorServices;

    @Override
    public void create(UserRequest userRequest) {
        validatorServices.validate(userRequest);

        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists");
        }


        Users users = new Users();
        users.setName(userRequest.getName());
        users.setUsername(userRequest.getUsername());
        users.setPassword(BCrypt.hashpw(userRequest.getPassword(), BCrypt.gensalt()));
        users.setAge(userRequest.getAge());


        userRepository.save(users);

        List<Address> addresses = userRequest.getAddresses().stream().map(addressRequest -> {
            Address address = new Address();
            address.setStreet(addressRequest.getStreet());
            address.setCity(addressRequest.getCity());
            address.setState(addressRequest.getState());
            address.setZip_code(addressRequest.getZip_code());
            address.setCountry(addressRequest.getCountry());
            address.setUser(users);
            return address;
        }).collect(Collectors.toList());

        addressRepository.saveAll(addresses);

        users.setAddresses(addresses);


        userRepository.save(users);
    }

    @Override
    public WebResponse<List<UserResponse>> list(UserListRequest userListRequest) {
        int currentPage = (userListRequest.getCurrentPage() != null && userListRequest.getCurrentPage() >= 0)
                ? userListRequest.getCurrentPage()
                : 0;

        int itemPerPage = (userListRequest.getItemPerPage() != null && userListRequest.getItemPerPage() > 0)
                ? userListRequest.getItemPerPage()
                : 10;

        Page<Users> page = userRepository.findAll(PageRequest.of(currentPage, itemPerPage));

        List<UserResponse> userResponses = page.getContent().stream()
                .map(users -> {
                    List<AddressResponse> addressResponses = users.getAddresses().stream()
                            .map(address -> AddressResponse.builder()
                                    .state(address.getStreet())
                                    .city(address.getCity())
                                    .state(address.getState())
                                    .zip_code(address.getZip_code())
                                    .country(address.getCountry())
                                    .created_at(address.getCreated_at())
                                    .update_at(address.getUpdate_at())
                                    .build())
                            .toList();

                    return UserResponse.builder()
                            .name(users.getName())
                            .username(users.getUsername())
                            .password(users.getPassword())
                            .age(users.getAge())
                            .created_at(users.getCreated_at())
                            .update_at(users.getUpdate_at())
                            .address(addressResponses)
                            .build();
                }).toList();

         return WebResponse.<List<UserResponse>>builder()
                 .message("OK")
                 .data(userResponses)
                 .currentPages(currentPage)
                 .itemPerPage(itemPerPage)
                 .totalPages(page.getTotalPages())
                 .totalItems(page.getTotalElements())
                 .build();

    }


}
