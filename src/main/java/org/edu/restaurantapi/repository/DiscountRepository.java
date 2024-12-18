package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.Branch;
import org.edu.restaurantapi.model.Discount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {

    Page<Discount> findByCodeContainingAndActiveTrue(String code, Pageable pageable);

    Discount findByCodeAndActiveTrue(String code);

    Discount findByCodeAndIdNotAndActiveTrue(String code, Long id);

    Page<Discount> findDiscountsByBranchIdAndActiveTrue(Long branchId, Pageable pageable);

    @Query("SELECT MONTH(d.createDate) AS month, COUNT(d) AS discountCount " +
            "FROM discounts d " +
            "WHERE YEAR(d.createDate) = YEAR(CURRENT_DATE) " +
            "GROUP BY MONTH(d.createDate) " +
            "ORDER BY MONTH(d.createDate) ASC")
    Page<Map<String, Object>> getDiscountStatsByMonth(Pageable pageable);

    Discount findDiscountsByCode(String code);

    @Query("SELECT d FROM discounts d WHERE " +
            "d.branch.id = :branchId AND " +
            ":month BETWEEN d.startDate AND d.endDate")
    Page<Discount> findDiscountsByMonth(Long branchId,
                                        LocalDate month,
                                        Pageable pageable);


}
