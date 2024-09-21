package org.edu.restaurantapi.controller;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.edu.restaurantapi.request.EmailRequest;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public String sendEmail(@Valid @RequestBody EmailRequest emailRequest) {
        emailService.sendEmail(emailRequest);
        return "Email sent successfully!";
    }

    @PostMapping("/send-html")
    public ResponseEntity<?> sendHtmlEmail(@Valid @RequestBody EmailRequest emailRequest) {
        try {
            String htmlContent = buildPasswordResetEmailContent(emailRequest.getText());  // Hàm build HTML content
            emailService.sendHtmlEmail(emailRequest, htmlContent);
            return ResponseEntity.ok().body(new ApiResponse<>(200, true, "Email sent successfully!", null));
        } catch (MessagingException e) {
            return ResponseEntity.internalServerError().body(new ApiResponse<>(500, false, "Error sending email: " + e.getMessage(), null));
        }
    }

    private String buildPasswordResetEmailContent(String verificationCode) {
        return "<html>" +
                "<body style='background-color: #121212; color: #e4c590; font-family: Arial, sans-serif; text-align: center; padding: 30px;'>" +
                "<div style='max-width: 600px; margin: auto; background-color: #1e1e1e; border-radius: 10px; padding: 30px; box-shadow: 0 4px 20px rgba(255,255,255,0.2);'>" +
                "<h1 style='color: #e4c590; margin-bottom: 20px;'>Đặt lại mật khẩu</h1>" +
                "<p>Đã nhận được yêu cầu đặt lại mật khẩu.</p>" +
                "<h2 style='color: #e4c590; font-weight: bold;'>" + verificationCode + "</h2>" +
                "<p>Nhập mã trên trang đặt lại mật khẩu.</p>" +
                "<p>Nếu không phải bạn, hãy bỏ qua email này.</p>" +
                "<p style='margin-top: 20px;'>Cảm ơn!</p>" +
                "<div style='margin-top: 30px; color: #ccc; font-size: 14px;'>" +
                "<p><strong>Tên Công Ty</strong></p>" +
                "<p>Liên hệ: support@yourcompany.com</p>" +
                "<p>Điện thoại: (123) 456-7890</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }


}