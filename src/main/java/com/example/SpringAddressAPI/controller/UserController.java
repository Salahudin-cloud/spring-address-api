package com.example.SpringAddressAPI.controller;

import com.example.SpringAddressAPI.dto.WebResponse;
import com.example.SpringAddressAPI.dto.user.*;
import com.example.SpringAddressAPI.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserServices userServices;


    @PostMapping(
            path = "/users",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> create(@RequestBody UserRequest userRequest) {
        userServices.create(userRequest);
        return WebResponse.<String>builder().message("OK").build();
    }

    @GetMapping(
            path = "/users",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<UserResponse>> list(
            @RequestParam(value = "currentPage", defaultValue = "0") Integer currentPage,
            @RequestParam(value = "itemPerPage", defaultValue = "10") Integer itemPerPage,
            UserListRequest userListRequest){
        return userServices.list(userListRequest);
    }

    @PatchMapping(
            path = "/users",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<UserUpdateResponse> update(
            @RequestParam Long id,
            @RequestBody UserUpdateRequest userUpdateRequest
            ){
        UserUpdateResponse data =  userServices.update(id, userUpdateRequest);
        return WebResponse.<UserUpdateResponse>builder()
                .message("OK")
                .data(data)
                .build();
    }

    @DeleteMapping(
            path = "/users"
    )
    public WebResponse<String> delete(@RequestParam Long id) {
        userServices.delete(id);
        return WebResponse.<String>builder()
                .message("OK")
                .build();
    }
}
