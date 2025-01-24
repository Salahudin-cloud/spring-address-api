package com.example.SpringAddressAPI.dto.user;

import com.example.SpringAddressAPI.dto.address.AddressRequest;
import com.example.SpringAddressAPI.entity.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {


    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 50)
    private String username;

    @NotBlank
    @Size(max = 255)
    private String password;

    @NotNull
    private Long age;

    @NotNull
    private List<AddressRequest> addresses;
}
