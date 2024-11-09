package org.edu.restaurantapi.service;

import org.edu.restaurantapi.model.Branch;
import org.edu.restaurantapi.model.BranchStatus;
import org.edu.restaurantapi.repository.BranchRepository;
import org.edu.restaurantapi.repository.BranchStatusRepository;
import org.edu.restaurantapi.request.BranchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class BranchService {

    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private BranchStatusRepository branchStatusRepository;

    public Page<Branch> getAllBranches(Optional<String> name, String phoneNumber, Pageable pageable) {
        Pageable pageableSorted = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "Id"));
        if (name.isPresent()) {
            return branchRepository.findByIsDeleteFalseAndNameContainingOrIsDeleteFalseAndPhoneNumberContaining(name.get(), phoneNumber, pageableSorted);
        } else {
            return branchRepository.findByIsDeleteFalse(pageableSorted);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
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
                .isDelete(false)
                .branchStatus(branchStatusRepository.findById(request.getBranchStatus()).get())
                .build();
        return branchRepository.save(branch);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Branch updateBranch(Long id, BranchRequest request) {
        Optional<BranchStatus> branchStatus = branchStatusRepository.findById(request.getBranchStatus());

        return branchRepository.findById(id).map(branch -> {
            branch.setName(request.getName() != null ? request.getName() : branch.getName());
            branch.setPhoneNumber(request.getPhoneNumber() != null ? request.getPhoneNumber() : branch.getPhoneNumber());
            branch.setAddress(request.getAddress() != null ? request.getAddress() : branch.getAddress());
            branch.setWardName(request.getWardName() != null ? request.getWardName() : branch.getWardName());
            branch.setWardId(request.getWardId() != null ? request.getWardId() : branch.getWardId());
            branch.setDistrictName(request.getDistrictName() != null ? request.getDistrictName() : branch.getDistrictName());
            branch.setDistrictId(request.getDistrictId() != null ? request.getDistrictId() : branch.getDistrictId());
            branch.setProvinceName(request.getProvinceName() != null ? request.getProvinceName() : branch.getProvinceName());
            branch.setProvinceId(request.getProvinceId() != null ? request.getProvinceId() : branch.getProvinceId());

            // Cập nhật branchStatus nếu request có và tìm thấy branchStatus
            branchStatus.ifPresent(branch::setBranchStatus);

            return branchRepository.save(branch);
        }).orElse(null);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Boolean delete(Long id) {
        return branchRepository.findById(id).map(b -> {
            b.setIsDelete(true);
            branchRepository.save(b);
            return true;
        }).orElse(false);
    }

    public Boolean findByName(String name) {
        return branchRepository.findByNameAndIsDeleteFalse(name) != null;
    }

    public Boolean findByPhoneNumber(String phoneNumber) {
        return branchRepository.findByPhoneNumberAndIsDeleteFalse(phoneNumber) != null;
    }

    public Boolean findByNameAndIdNot(String name, Long id) {
        return branchRepository.findByNameAndIdNotAndIsDeleteFalse(name, id) != null;
    }

    public Boolean findByPhoneNumberAndIdNot(String phoneNumber, Long id) {
        return branchRepository.findByPhoneNumberAndIdNotAndIsDeleteFalse(phoneNumber, id) != null;
    }
}
