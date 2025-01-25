package com.example.SpringAddressAPI.controller;

import com.example.SpringAddressAPI.dto.WebResponse;
import com.example.SpringAddressAPI.dto.user.UserListRequest;
import com.example.SpringAddressAPI.dto.user.UserRequest;
import com.example.SpringAddressAPI.dto.user.UserResponse;
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

}
