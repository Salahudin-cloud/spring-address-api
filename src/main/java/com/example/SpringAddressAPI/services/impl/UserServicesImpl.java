package com.example.SpringAddressAPI.services.impl;

import com.example.SpringAddressAPI.dto.address.AddressRequest;
import com.example.SpringAddressAPI.dto.user.UserRequest;
import com.example.SpringAddressAPI.entity.Address;
import com.example.SpringAddressAPI.entity.Users;
import com.example.SpringAddressAPI.repository.AddressRepository;
import com.example.SpringAddressAPI.repository.UserRepository;
import com.example.SpringAddressAPI.services.UserServices;
import com.example.SpringAddressAPI.services.ValidatorServices;
import com.example.SpringAddressAPI.utils.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
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
        // Validate request
        validatorServices.validate(userRequest);

        if (userRepository.existsByUsername(userRequest.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists");
        }

        // Create a new user
        Users users = new Users();
        users.setName(userRequest.getName());
        users.setUsername(userRequest.getUsername());
        users.setPassword(BCrypt.hashpw(userRequest.getPassword(), BCrypt.gensalt()));
        users.setAge(userRequest.getAge());

        // Save the user entity first
        userRepository.save(users);

        // Create new address entities and associate them with the user
        List<Address> addresses = userRequest.getAddresses().stream().map(addressRequest -> {
            Address address = new Address();
            address.setStreet(addressRequest.getStreet());
            address.setCity(addressRequest.getCity());
            address.setState(addressRequest.getState());
            address.setZip_code(addressRequest.getZip_code());
            address.setCountry(addressRequest.getCountry());
            address.setUser(users);  // Associate the saved user
            return address;
        }).collect(Collectors.toList());

        // Save the address entities
        addressRepository.saveAll(addresses);

        // Optionally, associate the addresses with the user
        users.setAddresses(addresses);

        // Optionally, update the user with the addresses (not necessary if using cascade)
        userRepository.save(users);
    }


}
