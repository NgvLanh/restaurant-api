package org.edu.restaurantapi.service;

import org.edu.restaurantapi.model.Address;
import org.edu.restaurantapi.model.User;
import org.edu.restaurantapi.repository.AddressRepository;
import org.edu.restaurantapi.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    public Address createAddress(Address address){
//        Address addressExists = addressRepository
//                .findAddressByProvinceIdAndDistrictIdAndWardId(address.getProvinceId(),
//                address.getDistrictId(), address.getWardId());
//        if (addressExists == null) {
            return addressRepository.save(address);
//        }
//        return null;
    }

    public Page<Address> getAddresses(Pageable pageable) {
        return addressRepository.findAll(pageable);
    }

    public Address updateAddress(Long addressId, Address updatedAddress) {
        var response = addressRepository.findAll();
        response.forEach(e->{
            e.setDefaultAddress(false);
            addressRepository.save(e);
        });

        return addressRepository.findById(addressId).map(existingAddress -> {
            existingAddress.setAddress(updatedAddress.getAddress() != null ? updatedAddress.getAddress() : existingAddress.getAddress());
            existingAddress.setProvinceName(updatedAddress.getProvinceName() != null ? updatedAddress.getProvinceName() : existingAddress.getProvinceName());
            existingAddress.setDistrictName(updatedAddress.getDistrictName() != null ? updatedAddress.getDistrictName() : existingAddress.getDistrictName());
            existingAddress.setWardName(updatedAddress.getWardName() != null ? updatedAddress.getWardName() : existingAddress.getWardName());
            existingAddress.setDefaultAddress(updatedAddress.getDefaultAddress() != null ? updatedAddress.getDefaultAddress() : existingAddress.getDefaultAddress());

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

    public List<Address> getAddressByUserId(Long userId) {
        return addressRepository.findAddressByUserId(userId);
    }

}
