package com.example.SpringAddressAPI.services;

import com.example.SpringAddressAPI.dto.address.AddressRequest;
import com.example.SpringAddressAPI.dto.address.AddressResponse;
import com.example.SpringAddressAPI.dto.address.UpdateAddressRequest;

public interface AddressServices {
    public void create(AddressRequest addressRequest);

    public AddressResponse update(Long id,Long address_id, UpdateAddressRequest updateAddressRequest);
}
