package com.example.SpringAddressAPI.services;

import com.example.SpringAddressAPI.dto.WebResponse;
import com.example.SpringAddressAPI.dto.user.UserListRequest;
import com.example.SpringAddressAPI.dto.user.UserRequest;
import com.example.SpringAddressAPI.dto.user.UserResponse;

import java.util.List;

public interface UserServices {

    public void create(UserRequest userRequest);

    public WebResponse<List<UserResponse>> list(UserListRequest userListRequest);
}
