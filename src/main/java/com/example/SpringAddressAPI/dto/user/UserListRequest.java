package com.example.SpringAddressAPI.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserListRequest {
    private Integer currentPage;
    private Integer itemPerPage;
}
