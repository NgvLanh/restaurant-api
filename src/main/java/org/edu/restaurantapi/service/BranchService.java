package org.edu.restaurantapi.service;

import org.edu.restaurantapi.model.Branch;
import org.edu.restaurantapi.repository.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class BranchService {

    @Autowired
    private BranchRepository repository;

    public Page<Branch> gets(String name, String phoneNumber, Pageable pageable) {
        Pageable pageableSorted = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "Id"));
        return repository.findByIsDeleteFalseAndNameContainingOrIsDeleteFalseAndPhoneNumberContaining(name, phoneNumber, pageableSorted);
    }

    public Branch create(Branch request) {
        return repository.save(request);
    }

    public Branch update(Long id, Branch request) {
        return repository.findById(id).map(b -> {
            b.setName(request.getName() != null ? request.getName() : b.getName());
            b.setPhoneNumber(request.getPhoneNumber() != null ? request.getPhoneNumber() : b.getPhoneNumber());
            b.setAddress(request.getAddress() != null ? request.getAddress() : b.getAddress());
            b.setDistrict(request.getDistrict() != null ? request.getDistrict() : b.getDistrict());
            b.setCity(request.getCity() != null ? request.getCity() : b.getCity());
            b.setBranchStatus(request.getBranchStatus() != null ? request.getBranchStatus() : b.getBranchStatus());
            return repository.save(b);
        }).orElse(null);
    }

    public Boolean delete(Long id) {
        return repository.findById(id).map(b -> {
            b.setIsDelete(true);
            repository.save(b);
            return true;
        }).orElse(false);
    }

    public Boolean findByName(String name) {
        return repository.findByNameAndIsDeleteFalse(name) != null;
    }

    public Boolean findByPhoneNumber(String phoneNumber) {
        return repository.findByPhoneNumberAndIsDeleteFalse(phoneNumber) != null;
    }

    public Boolean findByNameAndIdNot(String name, Long id) {
        return repository.findByNameAndIdNotAndIsDeleteFalse(name, id) != null;
    }

    public Boolean findByPhoneNumberAndIdNot(String phoneNumber, Long id) {
        return repository.findByPhoneNumberAndIdNotAndIsDeleteFalse(phoneNumber, id) != null;
    }
}
