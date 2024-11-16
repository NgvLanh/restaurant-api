package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findAddressByUserId(Long userId);

    Address findAddressByProvinceIdAndDistrictIdAndWardId(String provinceId, String districtId, String wardId);
}
