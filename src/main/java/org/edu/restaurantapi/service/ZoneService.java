package org.edu.restaurantapi.service;

import org.edu.restaurantapi.model.Zone;
import org.edu.restaurantapi.repository.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ZoneService {

    @Autowired
    private ZoneRepository repository;

    public Page<Zone> getAllZones(Optional<String> name, Long branch, Pageable pageable) {
        Pageable pageableSorted = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
       if (name.isPresent()) {
           return repository.findByNameContainingAndBranchIdAndActiveTrue(name.get(), branch, pageableSorted);
       }
       return repository.findAll(pageableSorted);
    }

    public Zone create(Zone request) {
        return repository.save(request);
    }

    public Zone update(Long id, Zone request) {
        return repository.findById(id).map(b -> {
            b.setAddress(request.getAddress() != null ? request.getAddress() : b.getAddress());
            b.setName(request.getName() != null ? request.getName() : b.getName());
            return repository.save(b);
        }).orElse(null);
    }

    public Zone delete(Long id) {
        return repository.findById(id).map(b -> {
            b.setActive(false);
            return repository.save(b);
        }).orElse(null);
    }

    public Boolean findByName(String name) {
        return repository.findByNameAndActiveTrue(name) != null;
    }

    public Boolean findByNameAndIdNot(String name, Long id) {
        return repository.findByNameAndIdNotAndActiveTrue(name, id) != null;
    }
}
