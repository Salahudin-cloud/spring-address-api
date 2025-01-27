package com.example.SpringAddressAPI.services;

import com.example.SpringAddressAPI.dto.WebResponse;
import com.example.SpringAddressAPI.dto.address.AddressRequest;
import com.example.SpringAddressAPI.dto.address.AddressResponse;
import com.example.SpringAddressAPI.dto.address.UpdateAddressRequest;

import java.util.List;

public interface AddressServices {
    public void create(AddressRequest addressRequest);

    public AddressResponse update(Long id,Long address_id, UpdateAddressRequest updateAddressRequest);

    public void delete(Long id);

    public WebResponse<List<AddressResponse>> getAddresses(Long id);
}
