package org.edu.restaurantapi.service;

import org.edu.restaurantapi.model.Zone;
import org.edu.restaurantapi.model.TableStatus;
import org.edu.restaurantapi.model.Zone;
import org.edu.restaurantapi.repository.ZoneRepository;
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
    private ZoneRepository repository;

    public Page<Zone> gets(String name, String branch, Pageable pageable) {
        Pageable pageableSorted = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        return repository.findByNameContainingAndBranchId(name, Long.parseLong(branch), pageableSorted);
    }

    public Zone create(Zone request) {
        return repository.save(request);
    }

    public Zone update(Long id, Zone request) {
        return repository.findById(id).map(b -> {
            b.setAddress(request.getAddress() != null ? request.getAddress() : b.getAddress());
            b.setName(request.getName() != null ? request.getName() : b.getName());
            b.setColorCode(request.getColorCode() != null ? request.getColorCode() : b.getColorCode());
            return repository.save(b);
        }).orElse(null);
    }

    public Boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        }
        return !repository.existsById(id);
    }

    public Boolean findByName(String name) {
        return repository.findByName(name) != null;
    }

    public Boolean findByNameAndIdNot(String name, Long id) {
        return repository.findByNameAndIdNot(name, id) != null;
    }
}
