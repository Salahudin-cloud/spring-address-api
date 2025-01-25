package com.example.SpringAddressAPI.repository;

import com.example.SpringAddressAPI.entity.Address;
import com.example.SpringAddressAPI.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository  extends JpaRepository<Address, Long> {
    Optional<Address> findByIdAndUser(Long id, Users user);
}
