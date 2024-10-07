package org.edu.restaurantapi.service;

import org.edu.restaurantapi.model.TableStatus;
import org.edu.restaurantapi.model.Zone;
import org.edu.restaurantapi.repository.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class ZoneService {

    @Autowired
    private ZoneRepository zoneRepository;

    public Zone createZone(Zone zone) {
        return zoneRepository.save(zone);
    }

    public Zone updateZone(Long id, Zone zone) {
        return zoneRepository.findById(id).map(existingZone -> {
            existingZone.setAddress(zone.getAddress()
                    != null ? zone.getAddress() : existingZone.getAddress());
            existingZone.setAddressDetails(zone.getAddressDetails()
                    != null ? zone.getAddressDetails() : existingZone.getAddressDetails());
            return zoneRepository.save(existingZone);
        }).orElse(null);
    }

    public Page<Zone> getZones(Pageable pageable) {
        Pageable pageableSorted = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "Id"));
        return zoneRepository.findZoneByIsDeleteFalse(pageableSorted);
    }

    public Zone getZone(Long id) {
        return zoneRepository.findById(id).orElse(null);
    }

    public Page<Zone> getZoneByAddress(String address, Pageable pageable) {
        return zoneRepository.findByAddressContainingAndIsDeleteFalse(address, pageable);
    }

    public Boolean deleteZone(Long id) {
        return zoneRepository.findById(id).map(zone -> {
            zone.setIsDelete(true);
            zoneRepository.save(zone);
            return true;
        }).orElse(false);
    }

    // Check zone name existence
    public Boolean zoneNameExists(Zone zone) {
        return zoneRepository.findZoneByAddressAndIsDeleteFalse(zone.getAddress()).isPresent();
    }
}
