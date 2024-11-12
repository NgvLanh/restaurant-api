package org.edu.restaurantapi.repository;

import org.edu.restaurantapi.model.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OtpRepository extends JpaRepository<OTP, Long> {
    OTP findByOtpCode(String otpCode); // Tìm OTP theo mã
    OTP findByUserId(Long userId);
    List<OTP> findAllByOtpCode(String otpCode);

    @Query("SELECT o FROM otp o " +
            "WHERE o.user.id = :userId " +
            "AND o.id = (SELECT MAX(o2.id) " +
            "FROM otp o2 WHERE o2.user.id = :userId)")
    OTP findLatestOtpByUserId(@Param("userId") Long userId);
}
