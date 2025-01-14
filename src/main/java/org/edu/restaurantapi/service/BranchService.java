package org.edu.restaurantapi.service;
import org.edu.restaurantapi.model.Branch;
import org.edu.restaurantapi.model.Branch;
import org.edu.restaurantapi.model.BranchStatus;
import org.edu.restaurantapi.repository.BranchRepository;
import org.edu.restaurantapi.repository.BranchRepository;
import org.edu.restaurantapi.repository.BranchStatusRepository;
import org.edu.restaurantapi.repository.UserRepository;
import org.edu.restaurantapi.request.BranchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class BranchService {

    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private BranchStatusRepository branchStatusRepository;
    @Autowired
    private UserRepository userRepository;

    public Page<Branch> getAllBranches(Optional<String> name, Pageable pageable) {
        Pageable pageableSorted = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        if (name.isPresent()) {
            return branchRepository.findByNameContainingAndActiveTrue(name.get(), pageableSorted);
        } else {
            return branchRepository.findByActiveTrue(pageable);
        }
    }

    public Branch createBranch(BranchRequest request) {
        Branch branch = Branch
                .builder()
                .name(request.getName())
                .phoneNumber(request.getPhoneNumber())
                .provinceId(request.getProvinceId())
                .provinceName(request.getProvinceName())
                .districtId(request.getDistrictId())
                .districtName(request.getDistrictName())
                .wardId(request.getWardId())
                .wardName(request.getWardName())
                .address(request.getAddress())
                .branchStatus(branchStatusRepository.findById(request.getBranchStatus()).get())
                .build();
        return branchRepository.save(branch);
    }

    public Branch updateBranch(Long id, BranchRequest request) {
        Branch existingBranch = branchRepository.findById(id).orElseThrow(() -> new RuntimeException("Branch not found"));

        Branch branch = Branch
                .builder()
                .id(id)
                .name(request.getName() != null ? request.getName() : existingBranch.getName())
                .phoneNumber(request.getPhoneNumber() != null ? request.getPhoneNumber() : existingBranch.getPhoneNumber())
                .provinceId(request.getProvinceId() != null ? request.getProvinceId() : existingBranch.getProvinceId())
                .provinceName(request.getProvinceName() != null ? request.getProvinceName() : existingBranch.getProvinceName())
                .districtId(request.getDistrictId() != null ? request.getDistrictId() : existingBranch.getDistrictId())
                .districtName(request.getDistrictName() != null ? request.getDistrictName() : existingBranch.getDistrictName())
                .wardId(request.getWardId() != null ? request.getWardId() : existingBranch.getWardId())
                .wardName(request.getWardName() != null ? request.getWardName() : existingBranch.getWardName())
                .address(request.getAddress() != null ? request.getAddress() : existingBranch.getAddress())
                .user(request.getUser() != null ? userRepository.findById(request.getUser()).orElse(existingBranch.getUser()) : existingBranch.getUser())
                .branchStatus(request.getBranchStatus() != null ? branchStatusRepository.findById(request.getBranchStatus()).orElse(existingBranch.getBranchStatus()) : existingBranch.getBranchStatus())
                .build();

        return branchRepository.save(branch);
    }

    public Branch updateBranch01(Long id, Branch request) {
        Branch existingBranch = branchRepository.findById(id).orElseThrow(() -> new RuntimeException("Branch not found"));

        Branch branch = Branch
                .builder()
                .id(id)
                .name(request.getName() != null ? request.getName() : existingBranch.getName())
                .phoneNumber(request.getPhoneNumber() != null ? request.getPhoneNumber() : existingBranch.getPhoneNumber())
                .provinceId(request.getProvinceId() != null ? request.getProvinceId() : existingBranch.getProvinceId())
                .provinceName(request.getProvinceName() != null ? request.getProvinceName() : existingBranch.getProvinceName())
                .districtId(request.getDistrictId() != null ? request.getDistrictId() : existingBranch.getDistrictId())
                .districtName(request.getDistrictName() != null ? request.getDistrictName() : existingBranch.getDistrictName())
                .wardId(request.getWardId() != null ? request.getWardId() : existingBranch.getWardId())
                .wardName(request.getWardName() != null ? request.getWardName() : existingBranch.getWardName())
                .address(request.getAddress() != null ? request.getAddress() : existingBranch.getAddress())
                .user(request.getUser() != null ? request.getUser() : existingBranch.getUser())
                .branchStatus(request.getBranchStatus() != null ? request.getBranchStatus() : existingBranch.getBranchStatus())
                .build();

        return branchRepository.save(branch);
    }

    public Branch deleteBranch(Long id) {
       return branchRepository.findById(id).map(branch -> {
           branch.setActive(false);
           return branchRepository.save(branch);
       }).orElse(null);
    }

    public Boolean findByName(String name) {
        return branchRepository.findByNameAndActiveTrue(name) != null;
    }

    public Boolean findByPhoneNumber(String name) {
        return branchRepository.findByPhoneNumberAndActiveTrue(name) != null;
    }

    public Boolean findByNameAndIdNot(String name, Long id) {
        return branchRepository.findByNameAndActiveTrueAndIdNot(name, id) != null;
    }

    public Boolean findByPhoneNumberAndIdNot(String name, Long id) {
        return branchRepository.findByPhoneNumberAndActiveTrueAndIdNot(name, id) != null;
    }

    public Page<Map<String, Object>> getBranchStatisticsByStatus(Pageable pageable) {
        return branchRepository.getBranchStatisticsByStatus(pageable);
    }
  
    public Branch findByUserId(Long userId) {
        return branchRepository.findByUserId(userId);
    }
}
