package com.example.SpringAddressAPI.services;

import com.example.SpringAddressAPI.dto.WebResponse;
import com.example.SpringAddressAPI.dto.user.*;

import java.util.List;

public interface UserServices {

    public void create(UserRequest userRequest);

    public WebResponse<List<UserResponse>> list(UserListRequest userListRequest);

    public UserUpdateResponse update(Long id , UserUpdateRequest userUpdateRequest);

    public void delete(Long id);

    public WebResponse<UserResponse> get(Long id);
}
