package com.example.SpringAddressAPI.services.impl;

import com.example.SpringAddressAPI.dto.address.AddressRequest;
import com.example.SpringAddressAPI.entity.Address;
import com.example.SpringAddressAPI.repository.AddressRepository;
import com.example.SpringAddressAPI.services.AddressServices;
import com.example.SpringAddressAPI.services.ValidatorServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServicesImpl implements AddressServices {

    @Autowired
    private ValidatorServices validatorServices;

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
}
