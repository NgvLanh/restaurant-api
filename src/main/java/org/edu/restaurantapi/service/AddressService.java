package org.edu.restaurantapi.service;

import org.edu.restaurantapi.model.Address;
import org.edu.restaurantapi.model.User;
import org.edu.restaurantapi.repository.AddressRepository;
import org.edu.restaurantapi.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    public Address createAddress(Address address){
        return addressRepository.save(address);
    }

    public Page<Address> getAddresses(Pageable pageable) {
        return addressRepository.findAll(pageable);
    }

    public Address updateAddress(Long addressId, Address updatedAddress) {
        return addressRepository.findById(addressId).map(existingAddress -> {
            existingAddress.setAddress(updatedAddress.getAddress() != null ? updatedAddress.getAddress() : existingAddress.getAddress());
            existingAddress.setCity(updatedAddress.getCity() != null ? updatedAddress.getCity() : existingAddress.getCity());
            existingAddress.setDistrict(updatedAddress.getDistrict() != null ? updatedAddress.getDistrict() : existingAddress.getDistrict());
            existingAddress.setWard(updatedAddress.getWard() != null ? updatedAddress.getWard() : existingAddress.getWard());
            existingAddress.setStatus(updatedAddress.getStatus() != null ? updatedAddress.getStatus() : existingAddress.getStatus());

            // Save the updated address
            addressRepository.save(existingAddress);
            return existingAddress;
        }).orElse(null);
    }

    public Boolean deleteAddress(Long id) {
        if (addressRepository.existsById(id)) {
            addressRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Address getAddress(Long id) {
        return addressRepository.findById(id).orElse(null);
    }





}
