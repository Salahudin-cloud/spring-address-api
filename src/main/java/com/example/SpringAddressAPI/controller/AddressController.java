package com.example.SpringAddressAPI.controller;

import com.example.SpringAddressAPI.dto.WebResponse;
import com.example.SpringAddressAPI.dto.address.AddressRequest;
import com.example.SpringAddressAPI.dto.address.AddressResponse;
import com.example.SpringAddressAPI.dto.address.UpdateAddressRequest;
import com.example.SpringAddressAPI.dto.user.UserListRequest;
import com.example.SpringAddressAPI.dto.user.UserResponse;
import com.example.SpringAddressAPI.services.AddressServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class AddressController {

    @Autowired
    private AddressServices addressServices;

    @PostMapping(
            path = "/address",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<String> create(@RequestBody AddressRequest addressRequest){
        addressServices.create(addressRequest);
        return WebResponse.<String>builder().message("OK").build();
    }

    @PatchMapping(
            path = "/address",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public AddressResponse update(@RequestParam Long id,
                                  @RequestParam Long address_id,
                                  @RequestBody UpdateAddressRequest updateAddressRequest){
        return  addressServices.update(id, address_id, updateAddressRequest);
    }



}
