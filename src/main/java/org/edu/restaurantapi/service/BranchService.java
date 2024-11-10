package org.edu.restaurantapi.service;
import org.edu.restaurantapi.model.Branch;
import org.edu.restaurantapi.model.Branch;
import org.edu.restaurantapi.model.BranchStatus;
import org.edu.restaurantapi.repository.BranchRepository;
import org.edu.restaurantapi.repository.BranchRepository;
import org.edu.restaurantapi.repository.BranchStatusRepository;
import org.edu.restaurantapi.request.BranchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class BranchService {

    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private BranchStatusRepository branchStatusRepository;

    public Page<Branch> getAllBranches(Optional<String> name, Pageable pageable) {
        Pageable pageableSorted = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        if (name.isPresent()) {
            return branchRepository.findByNameContainingAndIsDeleteFalse(name.get(), pageableSorted);
        } else {
            return branchRepository.findByIsDeleteFalse(pageable);
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
                .isDelete(false)
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
                .branchStatus(request.getBranchStatus() != null ? branchStatusRepository.findById(request.getBranchStatus()).orElse(existingBranch.getBranchStatus()) : existingBranch.getBranchStatus())
                .isDelete(existingBranch.getIsDelete())
                .build();

        return branchRepository.save(branch);
    }

    public Boolean deleteBranch(Long id) {
        if (branchRepository.existsById(id)) {
            branchRepository.deleteById(id);
        }
        return !branchRepository.existsById(id);
    }

    public Boolean findByName(String name) {
        return branchRepository.findByNameAndIsDeleteFalse(name) != null;
    }

    public Boolean findByPhoneNumber(String name) {
        return branchRepository.findByPhoneNumberAndIsDeleteFalse(name) != null;
    }

    public Boolean findByNameAndIdNot(String name, Long id) {
        return branchRepository.findByNameAndIsDeleteFalseAndIdNot(name, id) != null;
    }

    public Boolean findByPhoneNumberAndIdNot(String name, Long id) {
        return branchRepository.findByPhoneNumberAndIsDeleteFalseAndIdNot(name, id) != null;
    }
}
