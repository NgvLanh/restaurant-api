package org.edu.restaurantapi.service;

import org.edu.restaurantapi.model.BranchStatus;
import org.edu.restaurantapi.repository.BranchStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BranchStatusService {

    private final BranchStatusRepository branchStatusRepository;

    @Autowired
    public BranchStatusService(BranchStatusRepository branchStatusRepository) {
        this.branchStatusRepository = branchStatusRepository;
    }

    public Page<BranchStatus> getAllBranchStatuses(Pageable pageable) {
        return branchStatusRepository.findAll(pageable);
    }

    public BranchStatus getBranchStatusById(Long id) {
        return branchStatusRepository.findById(id).orElse(null);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public BranchStatus createBranchStatus(BranchStatus branchStatus) {
        Optional<BranchStatus> existingMethod = branchStatusRepository.findByName(branchStatus.getName());
        if (existingMethod.isPresent()) {
            return null;
        }
        return branchStatusRepository.save(branchStatus);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public BranchStatus updateBranchStatus(Long id, BranchStatus branchStatusUpdate) {
        return branchStatusRepository.findById(id).map(branchStatus -> {
            branchStatus.setName(branchStatusUpdate.getName() != null ? branchStatusUpdate.getName() : branchStatus.getName());
            return branchStatusRepository.save(branchStatus);
        }).orElse(null);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Boolean deleteBranchStatus(Long id) {
        if (branchStatusRepository.existsById(id)) {
            branchStatusRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<BranchStatus> findByName(String name) {
        return branchStatusRepository.findByName(name);
    }

    public Boolean branchStatusExist(BranchStatus branchStatus) {
        return branchStatusRepository.findByName(branchStatus.getName()).isPresent();
    }
}


