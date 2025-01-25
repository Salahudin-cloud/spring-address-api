package com.example.SpringAddressAPI.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateRequest {

    @Size(max = 100)
    private String name;

    @Size(max = 50)
    private String username;

    @Size(max = 255)
    private String password;

    private Long age;
}
