package com.example.SpringAddressAPI.services.impl;

import com.example.SpringAddressAPI.dto.user.UserRequest;
import com.example.SpringAddressAPI.entity.Address;
import com.example.SpringAddressAPI.entity.User;
import com.example.SpringAddressAPI.repository.AddressRepository;
import com.example.SpringAddressAPI.repository.UserRepository;
import com.example.SpringAddressAPI.services.UserServices;
import com.example.SpringAddressAPI.services.ValidatorServices;
import com.example.SpringAddressAPI.utils.BCrypt;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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

        if (userRepository.existByUsername(userRequest.getUsername())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exist");
        }

        User user = new User();
        user.setName(userRequest.getName());
        user.setUsername(userRequest.getUsername());
        user.setPassword(BCrypt.hashpw(userRequest.getPassword(), BCrypt.gensalt()));
        user.setAge(userRequest.getAge());

        userRepository.save(user);

        Long userId = user.getId();

        Address address = addressRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Address not found"));

        user.setAddress(address);

        userRepository.save(user);
    }
}
