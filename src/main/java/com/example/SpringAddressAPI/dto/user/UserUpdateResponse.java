package com.example.SpringAddressAPI.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateResponse {
    private Long id;
    private String name;
    private String username;
    private String password;
    private Long age;
    private Instant created_at;
    private Instant update_at;
}
