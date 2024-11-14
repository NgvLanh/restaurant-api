package org.edu.restaurantapi.service;

import org.edu.restaurantapi.model.Branch;
import org.edu.restaurantapi.model.Discount;
import org.edu.restaurantapi.repository.DiscountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;
import java.util.Map;


@Service
public class DiscountService {

    @Autowired
    private DiscountRepository repository;

    public Page<Discount> gets(String name, Pageable pageable) {
        Pageable pageableSorted = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        return repository.findByCodeContainingAndIsDeleteFalse(name, pageableSorted);
    }

    public Discount create(Discount request) {
        return repository.save(request);
    }

    public Discount update(Long id, Discount request) {
        return repository.findById(id).map(discount -> {
            discount.setQuantity(request.getQuantity() != null ? request.getQuantity() : discount.getQuantity());
            discount.setEndDate(request.getEndDate() != null ? request.getEndDate() : discount.getEndDate());
            discount.setStartDate(request.getStartDate() != null ? request.getStartDate() : discount.getStartDate());
            discount.setDiscountMethod(request.getDiscountMethod() != null ? request.getDiscountMethod() : discount.getDiscountMethod());
            discount.setQuota(request.getQuota() != null ? request.getQuota() : discount.getQuota());
            discount.setValue(request.getValue() != null ? request.getValue() : discount.getValue());
            discount.setIsDelete(request.getIsDelete() != null ? request.getIsDelete() : discount.getIsDelete());
            return repository.save(discount);
        }).orElse(null);
    }


    public Boolean delete(Long id) {
       if (repository.existsById(id)) {
           repository.deleteById(id);
           return true;
       }
       return false;
    }

    public Boolean findByCode(String code) {
        return repository.findByCodeAndIsDeleteFalse(code) != null;
    }

    public Boolean findByCodeAndIdNot(String code, Long id) {
        return repository.findByCodeAndIdNotAndIsDeleteFalse(code, id) != null;
    }

    public Page<Discount> getAllDiscountsByBranchId(Optional<Long> branchId, Pageable pageable) {
        return repository.findDiscountsByBranchId(branchId.get(), pageable);
    }
  
    public Page<Map<String, Object>> getDiscountStatsByMonth(Pageable pageable) {
        return repository.getDiscountStatsByMonth(pageable);
    }

}
