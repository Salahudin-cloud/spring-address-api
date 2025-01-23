package com.example.SpringAddressAPI.services;

import com.example.SpringAddressAPI.dto.WebResponse;
import com.example.SpringAddressAPI.dto.user.UserRequest;
import com.example.SpringAddressAPI.dto.user.UserResponse;

public interface UserServices {

    public void create(UserRequest userRequest);
}
