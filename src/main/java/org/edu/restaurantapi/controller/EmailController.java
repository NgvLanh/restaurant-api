package org.edu.restaurantapi.controller;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.edu.restaurantapi.model.OTP;
import org.edu.restaurantapi.model.User;
import org.edu.restaurantapi.repository.OtpRepository;
import org.edu.restaurantapi.repository.UserRepository;
import org.edu.restaurantapi.request.EmailRequest;
import org.edu.restaurantapi.request.OtpRequest;
import org.edu.restaurantapi.request.ResetPasswordRequest;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.EmailService;
import org.edu.restaurantapi.service.UserService;
import org.edu.restaurantapi.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Autowired
    private OtpRepository otpRepository;

//    @PostMapping("/send-html")
//    public ResponseEntity<?> sendHtmlEmail(@Valid @RequestBody EmailRequest emailRequest) {
//        try {
//            String htmlContent = buildOtpEmailContent(emailRequest.getText());
//            emailService.sendHtmlEmail(emailRequest, htmlContent);
//            return ResponseEntity.ok().body(new ApiResponse<>(200, true, "Email đã được gửi thành công!", null));
//        } catch (MessagingException e) {
//            return ResponseEntity.internalServerError().body(new ApiResponse<>(500, false, "Lỗi khi gửi email: " + e.getMessage(), null));
//        }
//    }

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtpEmail(@Valid @RequestBody EmailRequest emailRequest) {
        try {
            // Kiểm tra xem email có tồn tại không
            Optional<User> userOptional = userService.userEmailExistsOTP(emailRequest.getTo());
            if (userOptional.isEmpty()) {
                return ResponseEntity.badRequest().body(new ApiResponse<>(400, false, "Email không tồn tại.", null));
            }

            User user = userOptional.get(); // Lấy thông tin người dùng nếu có

            // Tạo mã OTP
            String otp = emailService.generateOtp();

            // Xây dựng nội dung email với mã OTP
            String htmlContent = buildOtpEmailContent(otp);

            // Gửi email với mã OTP
            // Gọi phương thức với cả hai tham số
            emailService.sendHtmlEmail(new EmailRequest(emailRequest.getTo(), "Mã OTP", htmlContent), htmlContent); // Gọi với 2 tham số

            // Lưu OTP vào CSDL
            OTP otpEntity = new OTP();
            otpEntity.setOtpCode(otp);
            otpEntity.setUser(user); // Gán người dùng vào OTP
            otpEntity.setCreatedAt(LocalDateTime.now());
            otpEntity.setExpiresAt(LocalDateTime.now().plusMinutes(5)); // Hết hạn sau 5 phút

            // Lưu OTP vào repository
            otpRepository.save(otpEntity);

            return ResponseEntity.ok().body(new ApiResponse<>(200, true, "OTP đã được gửi thành công!", null));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi gửi OTP.");
        }
    }



    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        try {
            emailService.resetPassword(request); // Gọi phương thức resetPassword
            return ResponseEntity.ok("Mật khẩu đã được đặt lại thành công.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi khi đặt lại mật khẩu: " + e.getMessage());
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateOtp(@Valid @RequestBody OtpRequest otpRequest) {
        List<OTP> otps = otpRepository.findAllByOtpCode(otpRequest.getOtp());

        if (otps.isEmpty()) {
            // Trả về mã 200 với thông báo rằng OTP không hợp lệ
            return ResponseEntity.ok().body(new ApiResponse<>(200, false, "Mã OTP không hợp lệ", null));
        } else if (otps.size() > 1) {
            // Trả về mã 200 với thông báo rằng có nhiều OTP
            return ResponseEntity.ok().body(new ApiResponse<>(200, false, "Tìm thấy nhiều mã OTP", null));
        } else {
            OTP otp = otps.get(0);
            if (otp.getExpiresAt().isBefore(LocalDateTime.now())) {
                // Trả về mã 200 với thông báo rằng OTP đã hết hạn
                return ResponseEntity.ok().body(new ApiResponse<>(200, false, "Mã OTP đã hết hạn", null));
            }
            // Nếu OTP hợp lệ
            return ResponseEntity.ok().body(new ApiResponse<>(200, true, "Mã OTP hợp lệ", null));
        }
    }

    private String buildOtpEmailContent(String otp) {
        return "<html>" +
                "<body style='background-color: #121212; color: #e4c590; font-family: Arial, sans-serif; text-align: center; padding: 30px;'>" +
                "<div style='max-width: 600px; margin: auto; background-color: #1e1e1e; border-radius: 10px; padding: 30px; box-shadow: 0 4px 20px rgba(255,255,255,0.2);'>" +
                "<h1 style='color: #e4c590; margin-bottom: 20px;'>DELICI RESTAURANT</h1>" +
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
