package com.example.SpringAddressAPI.dto.user;

import com.example.SpringAddressAPI.dto.address.AddressResponse;
import com.example.SpringAddressAPI.entity.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private String name;
    private String username;
    private String password;
    private Long age;
    private Instant created_at;
    private Instant update_at;
    private List<AddressResponse> address;

}
