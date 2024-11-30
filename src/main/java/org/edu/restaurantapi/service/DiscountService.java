package org.edu.restaurantapi.service;

import org.edu.restaurantapi.model.Branch;
import org.edu.restaurantapi.model.Discount;
import org.edu.restaurantapi.repository.BranchRepository;
import org.edu.restaurantapi.repository.DiscountRepository;
import org.edu.restaurantapi.request.DiscountRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.Map;


@Service
public class DiscountService {

    @Autowired
    private DiscountRepository discountRepository;
    @Autowired
    private BranchRepository branchRepository;

    public Page<Discount> getAllDiscountsByBranchIdAndMonth(Long branchId, String month, Pageable pageable) {
        Pageable pageableSorted = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));

        if (month == null || month.isEmpty()) {
            return discountRepository.findDiscountsByBranchId(branchId, pageableSorted);
        }

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(month, inputFormatter);
        String formattedDate = date.format(outputFormatter);
        return discountRepository.findDiscountsByMonth(branchId, LocalDate.parse(formattedDate), pageableSorted);
    }

    public Discount createDiscount(DiscountRequest request) {
        Branch branch = branchRepository.findById(request.getBranchId()).orElse(null);
        Discount discount = Discount.builder()
                .branch(branch)
                .code(request.getCode())
                .discountMethod(request.getDiscountMethod())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .quantity(request.getQuantity())
                .quota(request.getQuota())
                .value(request.getValue())
                .build();
        return discountRepository.save(discount);
    }

    public Discount update(Long id, Discount request) {
        return discountRepository.findById(id).map(discount -> {
            discount.setQuantity(request.getQuantity() != null ? request.getQuantity() : discount.getQuantity());
            discount.setEndDate(request.getEndDate() != null ? request.getEndDate() : discount.getEndDate());
            discount.setStartDate(request.getStartDate() != null ? request.getStartDate() : discount.getStartDate());
            discount.setDiscountMethod(request.getDiscountMethod() != null ? request.getDiscountMethod() : discount.getDiscountMethod());
            discount.setQuota(request.getQuota() != null ? request.getQuota() : discount.getQuota());
            discount.setValue(request.getValue() != null ? request.getValue() : discount.getValue());
            discount.setIsDelete(request.getIsDelete() != null ? request.getIsDelete() : discount.getIsDelete());
            return discountRepository.save(discount);
        }).orElse(null);
    }


    public Boolean delete(Long id) {
        if (discountRepository.existsById(id)) {
            discountRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Boolean findByCode(String code) {
        return discountRepository.findByCodeAndIsDeleteFalse(code) != null;
    }

    public Boolean findByCodeAndIdNot(String code, Long id) {
        return discountRepository.findByCodeAndIdNotAndIsDeleteFalse(code, id) != null;
    }

    public Page<Discount> getAllDiscountsByBranchId(Optional<Long> branchId, Pageable pageable) {
        return discountRepository.findDiscountsByBranchId(branchId.get(), pageable);
    }

    public Page<Map<String, Object>> getDiscountStatsByMonth(Pageable pageable) {
        return discountRepository.getDiscountStatsByMonth(pageable);
    }

    public Discount checkDiscountCode(String code) {
        return discountRepository.findDiscountsByCode(code);
    }
}
