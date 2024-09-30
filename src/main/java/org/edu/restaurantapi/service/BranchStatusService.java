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

    @Autowired
    private BranchStatusRepository branchStatusRepository;

    public Page<BranchStatus> getAllBranchStatuses(Pageable pageable) {
        return branchStatusRepository.findBranchStatusByIsDeleteFalse(pageable);
    }

    public BranchStatus getBranchStatusById(Long id) {
        return branchStatusRepository.findById(id).orElse(null);
    }

    public BranchStatus createBranchStatus(BranchStatus branchStatus) {
        Optional<BranchStatus> existingMethod = branchStatusRepository.findByNameAndIsDeleteFalse(branchStatus.getName());
        if (existingMethod.isPresent()) {
            return null;
        }
        return branchStatusRepository.save(branchStatus);
    }

    public BranchStatus updateBranchStatus(Long id, BranchStatus branchStatusUpdate) {
        return branchStatusRepository.findById(id).map(branchStatus -> {
            branchStatus.setName(branchStatusUpdate.getName() != null ? branchStatusUpdate.getName() : branchStatus.getName());
            return branchStatusRepository.save(branchStatus);
        }).orElse(null);
    }

    public Boolean deleteBranchStatus(Long id) {
        return branchStatusRepository.findById(id).map(branchStatus -> {
            branchStatus.setIsDelete(true);
            branchStatusRepository.save(branchStatus);
            return true;
        }).orElse(false);
    }

    public Optional<BranchStatus> findByName(String name) {
        return branchStatusRepository.findByNameAndIsDeleteFalse(name);
    }

    public Boolean branchStatusNameExist(BranchStatus branchStatus) {
        return branchStatusRepository.findByNameAndIsDeleteFalse(branchStatus.getName()).isPresent();
    }
}
