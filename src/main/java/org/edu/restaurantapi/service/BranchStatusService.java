package org.edu.restaurantapi.service;

import org.edu.restaurantapi.model.BranchStatus;
import org.edu.restaurantapi.repository.BranchStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BranchStatusService {

    @Autowired
    private BranchStatusRepository repository;

    public Page<BranchStatus> getAllBranchStatus(Optional<String> name, Pageable pageable) {
        Pageable pageableSorted = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        if (name.isPresent()) {
            return repository.findByNameContaining(name.get(), pageableSorted);
        } else {
            return repository.findAll(pageable);
        }
    }

    public BranchStatus createBranchStatus(BranchStatus request) {
        return repository.save(request);
    }

    public BranchStatus updateBranchStatus(Long id, BranchStatus request) {
        return repository.findById(id).map(b -> {
            b.setName(request.getName() != null ? request.getName() : b.getName());
            b.setColorCode(request.getName() != null ? request.getColorCode() : b.getColorCode());
            return repository.save(b);
        }).orElse(null);
    }

    public Boolean deleteBranchStatus(Long id) {
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
