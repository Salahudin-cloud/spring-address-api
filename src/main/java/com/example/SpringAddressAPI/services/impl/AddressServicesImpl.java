package com.example.SpringAddressAPI.services.impl;

import com.example.SpringAddressAPI.dto.WebResponse;
import com.example.SpringAddressAPI.dto.address.AddressRequest;
import com.example.SpringAddressAPI.dto.address.AddressResponse;
import com.example.SpringAddressAPI.dto.address.UpdateAddressRequest;
import com.example.SpringAddressAPI.entity.Address;
import com.example.SpringAddressAPI.entity.Users;
import com.example.SpringAddressAPI.repository.AddressRepository;
import com.example.SpringAddressAPI.repository.UserRepository;
import com.example.SpringAddressAPI.services.AddressServices;
import com.example.SpringAddressAPI.services.ValidatorServices;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toList;

@Service
public class AddressServicesImpl implements AddressServices {

    @Autowired
    private ValidatorServices validatorServices;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public void create(AddressRequest addressRequest) {
        System.out.println(addressRequest);

        validatorServices.validate(addressRequest);

        Address address = new Address();
        address.setStreet(addressRequest.getStreet());
        address.setCity(addressRequest.getCity());
        address.setState(addressRequest.getState());
        address.setZip_code(addressRequest.getZip_code());
        address.setCountry(addressRequest.getCountry());

        addressRepository.save(address);
    }

    @Override
    public AddressResponse update(Long id, Long address_id, UpdateAddressRequest updateAddressRequest) {
        validatorServices.validate(updateAddressRequest);

        Users user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Address addressUpdate = addressRepository.findByIdAndUser(address_id, user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found"));

        Map<String, Consumer<String>> fieldToSetter = Map.of(
                "street", addressUpdate::setStreet,
                "city", addressUpdate::setCity,
                "state", addressUpdate::setState,
                "zip_code", addressUpdate::setZip_code,
                "country", addressUpdate::setCountry
        );

        if (updateAddressRequest.getStreet() != null) fieldToSetter.get("street").accept(updateAddressRequest.getStreet());
        if (updateAddressRequest.getCity() != null) fieldToSetter.get("city").accept(updateAddressRequest.getCity());
        if (updateAddressRequest.getState() != null) fieldToSetter.get("state").accept(updateAddressRequest.getState());
        if (updateAddressRequest.getZip_code() != null) fieldToSetter.get("zip_code").accept(updateAddressRequest.getZip_code());
        if (updateAddressRequest.getCountry() != null) fieldToSetter.get("country").accept(updateAddressRequest.getCountry());

        addressRepository.save(addressUpdate);

        return AddressResponse.builder()
                .street(addressUpdate.getStreet())
                .state(addressUpdate.getState())
                .city(addressUpdate.getCity())
                .country(addressUpdate.getCountry())
                .zip_code(addressUpdate.getZip_code())
                .build();
    }

    @Override
    public void delete(Long id) {
        Address address = addressRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found"));

        addressRepository.delete(address);
    }

    @Override
    public WebResponse<List<AddressResponse>> getAddresses(Long id) {

        List<Address> addresses = addressRepository.findByUserId(id)
                .orElseThrow(() -> new EntityNotFoundException("No addresses found for user id " + id));

        List<AddressResponse> addressResponses = addresses.stream().map(address ->
                AddressResponse.builder()
                        .id(address.getId())
                        .street(address.getStreet())
                        .state(address.getState())
                        .city(address.getCity())
                        .country(address.getCountry())
                        .zip_code(address.getZip_code())
                        .build()
        ).toList();

        return WebResponse.<List<AddressResponse>>builder()
                .message("OK")
                .data(addressResponses)
                .build();
    }

}
