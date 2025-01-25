package com.example.SpringAddressAPI.dto.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAddressRequest {


    @Size(max = 255)
    private String street;

    @Size(max = 100)
    private String city;


    @Size(max = 100)
    private String state;


    @Size(max = 20)
    private String zip_code;


    @Size(max = 100)
    private String country;

}
