package com.example.SpringAddressAPI.dto.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressResponse {

    private Long  id;
    private String street;

    private String city;

    private String state;

    private String zip_code;

    private String country;

    private Instant created_at;
    private Instant update_at;

}
