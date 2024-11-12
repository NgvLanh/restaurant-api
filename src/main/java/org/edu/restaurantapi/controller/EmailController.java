package org.edu.restaurantapi.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.edu.restaurantapi.model.OTP;
import org.edu.restaurantapi.model.User;
import org.edu.restaurantapi.repository.OtpRepository;
import org.edu.restaurantapi.request.EmailRequest;
import org.edu.restaurantapi.request.OtpRequest;
import org.edu.restaurantapi.request.ResetPasswordRequest;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.EmailService;
import org.edu.restaurantapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Slf4j
@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Autowired
    private OtpRepository otpRepository;


    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtpEmail(@Valid @RequestBody EmailRequest request) {
        try {
            Optional<User> userExists = userService.findUserByEmail(request.getTo());
            log.info(userExists.toString());
            if (userExists.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.BAD_REQUEST("Email này chưa đăng ký"));
            }
            User user = userExists.get();
            String otp = emailService.generateOtp();
            String htmlContent = buildOtpEmailContent(otp);
            EmailRequest emailRequest = EmailRequest
                    .builder()
                    .to(user.getEmail())
                    .subject("Khôi phục mật khẩu")
                    .text(htmlContent)
                    .build();

            emailService.sendHtmlEmail(emailRequest, htmlContent);

            OTP otpUser = new OTP();
            otpUser.setOtpCode(otp);
            otpUser.setUser(user);

            otpRepository.save(otpUser);
            return ResponseEntity.ok().body(ApiResponse.SUCCESS("Đã gửi mã OTP thành công vui lòng kiểm tra Email của bạn"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(ApiResponse.SERVER_ERROR("Lỗi gửi OTP"));
        }
    }

    @PostMapping("/password-recovery/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            Boolean recoveryPassword = emailService.resetPassword(request);
            if (recoveryPassword) {
                return ResponseEntity.ok(ApiResponse.SUCCESS("Mật khẩu đã được đặt lại thành công"));
            }
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Lỗi khi đặt lại mật khẩu"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi đặt lại mật khẩu: " + e.getMessage());
        }
    }

    @PostMapping("/password-recovery/verify-code")
    public ResponseEntity<?> validateOtp(@Valid @RequestBody OtpRequest otpRequest) {
        var userOpt = userService.findUserByEmail(otpRequest.getEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Người dùng không tồn tại"));
        }
        var user = userOpt.get();
        OTP otp = emailService.findLatestOtpByUserId(user.getId());
        if (otp == null) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Không tìm thấy mã OTP cho người dùng"));
        }
        if (!otp.getOtpCode().equals(otpRequest.getOtp())) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Mã OTP không hợp lệ"));
        }
        var now = LocalDateTime.now();
        if (otp.getExpiresAt().isBefore(now)) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Mã OTP của bạn đã hết hạn, vui lòng gửi lại mã mới"));
        }
        return ResponseEntity.ok(ApiResponse.SUCCESS("Xác thực OTP thành công"));
    }


    private String buildOtpEmailContent(String otp) {
        return "<html>" +
                "<body style='background-color: #121212; color: #e4c590; font-family: Arial, sans-serif; text-align: center; padding: 30px;'>" +
                "<div style='max-width: 600px; margin: auto; background-color: #1e1e1e; border-radius: 10px; padding: 30px; box-shadow: 0 4px 20px rgba(255,255,255,0.2);'>" +
                "<h1 style='color: #e4c590; margin-bottom: 20px;'>Cook Book</h1>" +
                "<p style='color: #e4c590;'>Mã OTP của bạn là:</p>" +
                "<h2 style='color: #e4c590; font-weight: bold;'>" + otp + "</h2>" +
                "<p style='color: #e4c590;'>Nhập mã trên trang xác thực.</p>" +
                "<p style='color: #e4c590;'>Nếu không phải bạn, hãy bỏ qua email này.</p>" +
                "<p style='margin-top: 20px; color: white; font-weight: bold; text-transform: uppercase;'>Cảm ơn!</p>" + // Chỉnh màu chữ và in hoa
                "<div style='margin-top: 30px; color: #ccc; font-size: 14px;'>" +
                "<p>Liên hệ: khuuphuc403@gmail.com</p>" +
                "<p>Điện thoại: 0388899524</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }


}
