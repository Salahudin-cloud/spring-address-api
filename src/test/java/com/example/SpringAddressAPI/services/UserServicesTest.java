package com.example.SpringAddressAPI.services;

import com.example.SpringAddressAPI.dto.WebResponse;
import com.example.SpringAddressAPI.dto.address.AddressRequest;
import com.example.SpringAddressAPI.dto.user.UserRequest;
import com.example.SpringAddressAPI.dto.user.UserResponse;
import com.example.SpringAddressAPI.entity.Address;
import com.example.SpringAddressAPI.entity.Users;
import com.example.SpringAddressAPI.repository.AddressRepository;
import com.example.SpringAddressAPI.repository.UserRepository;
import com.example.SpringAddressAPI.services.impl.UserServicesImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class UserServicesTest {

    @InjectMocks
    private UserServicesImpl userServices;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private ValidatorServices validatorServices;

    @Test
    public void createUser(){
        UserRequest userRequest = new UserRequest(
                "John Doe",
                "john",
                "password123",
                30L,
                List.of(new AddressRequest("123 street", "country", "state country", "123", "Country123"))
        );

        List<Address> addressRequests = userRequest.getAddresses().stream().map(
                addressRequest -> new Address(
                        null,
                        addressRequest.getStreet(),
                        addressRequest.getCity(),
                        addressRequest.getState(),
                        addressRequest.getZip_code(),
                        addressRequest.getCountry(),
                        null,
                        null,
                        null
                )).toList();

        Users users = new Users(
                1L,
                "John Doe",
                "john",
                "password123",
                30L,
                null,
                null,
                addressRequests
        );

        doNothing().when(validatorServices).validate(any());


        when(userRepository.save(any(Users.class))).thenReturn(users);
        when(addressRepository.saveAll(anyList())).thenReturn(addressRequests);


        userServices.create(userRequest);

        verify(validatorServices, times(1)).validate(userRequest);
        verify(userRepository, times(2)).save(any(Users.class));
        verify(addressRepository, times(1)).saveAll(anyList());

        assertNotNull(users.getAddresses());
        assertEquals(1, users.getAddresses().size());
    }

    @Test
    public void testGetUserById() {

        Users user = new Users(
                1L,
                "John Doe",
                "john",
                "password123",
                30L,
                null,
                null,
                List.of(
                        new Address(
                                1L,
                                "123 street",
                                "Los Angeles",
                                "LA",
                                "123",
                                "United States",
                                null,
                                null,
                                null
                        )
                )
        );

        when(userRepository.getReferenceById(1L)).thenReturn(user);

        WebResponse<UserResponse> fetchedUser = userServices.get(1L);

        assertNotNull(fetchedUser);
        assertEquals("John Doe", fetchedUser.getData().getName());
        assertEquals("OK", fetchedUser.getMessage());
        assertEquals("john", fetchedUser.getData().getUsername());
        assertEquals("123 street", fetchedUser.getData().getAddress().getFirst().getStreet());
        assertEquals("Los Angeles", fetchedUser.getData().getAddress().getFirst().getCity());
        assertEquals("LA", fetchedUser.getData().getAddress().getFirst().getState());
        assertEquals("123", fetchedUser.getData().getAddress().getFirst().getZip_code());
        assertEquals("United States", fetchedUser.getData().getAddress().getFirst().getCountry());
    }



}