package org.edu.restaurantapi.service;

import org.edu.restaurantapi.model.Branch;
import org.edu.restaurantapi.repository.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BranchService {

    @Autowired
    private BranchRepository branchRepository;

    // Thêm
    @PreAuthorize("hasRole('ADMIN')")
    public Branch createBranch(Branch branch) {
        Optional<Branch> existingBranch = branchRepository.findByPhoneNumber(branch.getPhoneNumber());
        if (existingBranch.isPresent()) {
            return null;  // Trả về null nếu chi nhánh đã tồn tại
        }
        return branchRepository.save(branch);
    }

    // Xem
    @PreAuthorize("hasRole('ADMIN')")
    public Page<Branch> getAllBranches(Pageable pageable) {
        return branchRepository.findAll(pageable);
    }

    // Tìm
    public Branch getBranchById(Long id) {
        return branchRepository.findById(id).orElse(null);
    }

    // Cập nhật
    @PreAuthorize("hasRole('ADMIN')")
    public Branch updateBranch(Long id, Branch branchDetails) {
        return branchRepository.findById(id).map(existingBranch -> {
            existingBranch.setName(branchDetails.getName() != null ? branchDetails.getName() : existingBranch.getName());
            existingBranch.setPhoneNumber(branchDetails.getPhoneNumber() != null ? branchDetails.getPhoneNumber() : existingBranch.getPhoneNumber());
            existingBranch.setAddress(branchDetails.getAddress() != null ? branchDetails.getAddress() : existingBranch.getAddress());
            existingBranch.setDistrict(branchDetails.getDistrict() != null ? branchDetails.getDistrict() : existingBranch.getDistrict());
            existingBranch.setCity(branchDetails.getCity() != null ? branchDetails.getCity() : existingBranch.getCity());
            existingBranch.setBranchStatus(branchDetails.getBranchStatus() != null ? branchDetails.getBranchStatus() : existingBranch.getBranchStatus());
            return branchRepository.save(existingBranch);
        }).orElseThrow(() -> new RuntimeException("Không tìm thấy id này " + id));
    }

    // Xóa
    @PreAuthorize("hasRole('ADMIN')")
    public boolean deleteBranch(Long id) {
        if (branchRepository.existsById(id)) {
            branchRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Kiểm tra
    public boolean branchPhoneNumberExists(Branch branch) {
        return branchRepository.findByPhoneNumber(branch.getPhoneNumber()).isPresent();
    }
}
