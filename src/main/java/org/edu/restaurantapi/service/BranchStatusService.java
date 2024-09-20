package org.edu.restaurantapi.service;

import org.edu.restaurantapi.model.BranchStatus;
import org.edu.restaurantapi.repository.BranchStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    public List<BranchStatus> getAllBranchStatuses() {
        return branchStatusRepository.findAll();
    }

    public Optional<BranchStatus> getBranchStatusById(Long id) {
        return branchStatusRepository.findById(id);
    }

    public BranchStatus createBranchStatus(BranchStatus branchStatus) {
        return branchStatusRepository.save(branchStatus);
    }

    public Optional<BranchStatus> updateBranchStatus(Long id, BranchStatus branchStatusUpdate) {
        return branchStatusRepository.findById(id).map(branchStatus -> {
            branchStatus.setName(branchStatusUpdate.getName());
            return branchStatusRepository.save(branchStatus);
        });
    }

    public void deleteBranchStatus(Long id) {
        branchStatusRepository.deleteById(id);
    }
}
