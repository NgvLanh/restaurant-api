package org.edu.restaurantapi.service;

import org.edu.restaurantapi.model.Branch;
import org.edu.restaurantapi.repository.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class BranchService {

    @Autowired
    private BranchRepository branchRepository;

    public Branch createBranch(Branch branch) {
        Optional<Branch> existingBranch = branchRepository.findByPhoneNumberAndIsDeleteFalse(branch.getPhoneNumber());
        if (existingBranch.isPresent()) {
            return null;  // Trả về null nếu chi nhánh đã tồn tại
        }
        return branchRepository.save(branch);
    }

    public Page<Branch> getAllBranches(Pageable pageable) {
        Pageable pageableSorted = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "Id"));
        return branchRepository.findBranchByIsDeleteFalse(pageableSorted);
    }

    public Branch getBranchById(Long id) {
        return branchRepository.findById(id).orElse(null);
    }

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

    public boolean deleteBranch(Long id) {
        return branchRepository.findById(id).map(branch -> {
            branch.setIsDelete(true);
            branchRepository.save(branch);
            return true;
        }).orElse(false);
    }

    public boolean branchPhoneNumberExists(Branch branch) {
        return branchRepository.findByPhoneNumberAndIsDeleteFalse(branch.getPhoneNumber()).isPresent();
    }

    public boolean branchNameExists(Branch branch) {
        return branchRepository.findByNameAndIsDeleteFalse(branch.getPhoneNumber()).isPresent();
    }
}
