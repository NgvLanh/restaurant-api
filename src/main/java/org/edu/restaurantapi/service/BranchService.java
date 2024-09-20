package org.edu.restaurantapi.service;

import org.edu.restaurantapi.model.Branch;
import org.edu.restaurantapi.repository.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BranchService {

    private final BranchRepository branchRepository;

    @Autowired
    public BranchService(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    // Thêm
    public Branch createBranch(Branch branch) {
        return branchRepository.save(branch);
    }

    // Xem
    public List<Branch> getAllBranches() {
        return branchRepository.findAll();
    }

// Tìm
    public Optional<Branch> getBranchById(Long id) {
        return branchRepository.findById(id);
    }

    // Cập nhật
    public Branch updateBranch(Long id, Branch branchDetails) {
        return branchRepository.findById(id).map(branch -> {
            branch.setName(branchDetails.getName());
            branch.setPhoneNumber(branchDetails.getPhoneNumber());
            branch.setAddress(branchDetails.getAddress());
            branch.setDistrict(branchDetails.getDistrict());
            branch.setCity(branchDetails.getCity());
            branch.setBranchStatus(branchDetails.getBranchStatus());
            return branchRepository.save(branch);
        }).orElseThrow(() -> new RuntimeException("Không tìm thấy id này " + id));
    }

    // Xóa
    public void deleteBranch(Long id) {
        branchRepository.deleteById(id);
    }
}
