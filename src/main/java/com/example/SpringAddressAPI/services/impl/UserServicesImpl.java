package com.example.SpringAddressAPI.services.impl;

import com.example.SpringAddressAPI.dto.WebResponse;
import com.example.SpringAddressAPI.dto.address.AddressResponse;
import com.example.SpringAddressAPI.dto.user.*;
import com.example.SpringAddressAPI.entity.Address;
import com.example.SpringAddressAPI.entity.Users;
import com.example.SpringAddressAPI.repository.AddressRepository;
import com.example.SpringAddressAPI.repository.UserRepository;
import com.example.SpringAddressAPI.services.UserServices;
import com.example.SpringAddressAPI.services.ValidatorServices;
import com.example.SpringAddressAPI.utils.BCrypt;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class UserServicesImpl implements UserServices {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ValidatorServices validatorServices;

    @Transactional
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
                                    .id(address.getId())
                                    .street(address.getStreet())
                                    .city(address.getCity())
                                    .state(address.getState())
                                    .zip_code(address.getZip_code())
                                    .country(address.getCountry())
                                    .created_at(address.getCreated_at())
                                    .update_at(address.getUpdate_at())
                                    .build())
                            .toList();

                    return UserResponse.builder()
                            .id(users.getId())
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

    @Override
    public UserUpdateResponse update(Long id , UserUpdateRequest userUpdateRequest) {
        validatorServices.validate(userUpdateRequest);

        Users users = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));


        Map<String, Consumer<String>> fieldToSetter = Map.of(
                "name", users::setName,
                "username", users::setUsername,
                "password", users::setPassword,
                "age", value -> users.setAge(Long.parseLong(value))
        );

        if (userUpdateRequest.getName() != null) fieldToSetter.get("name").accept(userUpdateRequest.getName());
        if (userUpdateRequest.getUsername() != null) fieldToSetter.get("username").accept(userUpdateRequest.getUsername());
        if (userUpdateRequest.getPassword() != null) fieldToSetter.get("password").accept(BCrypt.hashpw(userUpdateRequest.getPassword(), BCrypt.gensalt()));
        if (userUpdateRequest.getAge() != null) fieldToSetter.get("age").accept(String.valueOf(userUpdateRequest.getAge()));

        userRepository.save(users);

        return UserUpdateResponse.builder()
                .id(users.getId())
                .name(users.getName())
                .username(users.getUsername())
                .password(users.getPassword())
                .age(users.getAge())
                .created_at(users.getCreated_at())
                .update_at(users.getUpdate_at())
                .build();
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Users users = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        userRepository.delete(users);
    }

    @Override
    public WebResponse<UserResponse> get(Long id) {
        try {
            Users users = userRepository.getReferenceById(id);
            List<AddressResponse> addressResponses = users.getAddresses().stream().map(
                    address -> AddressResponse.builder()
                            .id(address.getId())
                            .street(address.getStreet())
                            .city(address.getCity())
                            .state(address.getState())
                            .zip_code(address.getZip_code())
                            .country(address.getCountry())
                            .created_at(address.getCreated_at())
                            .update_at(address.getUpdate_at())
                            .build()
            ).toList();

            UserResponse userResponse = UserResponse.builder()
                    .id(users.getId())
                    .name(users.getName())
                    .username(users.getUsername())
                    .password(users.getPassword())
                    .age(users.getAge())
                    .created_at(users.getCreated_at())
                    .update_at(users.getUpdate_at())
                    .address(addressResponses)
                    .build();

            return WebResponse.<UserResponse>builder()
                    .message("OK")
                    .data(userResponse)
                    .build();
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("User with ID " + id + " not found.");
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred. Please try again later.");
        }


    }


}
