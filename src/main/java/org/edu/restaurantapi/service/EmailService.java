package org.edu.restaurantapi.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.edu.restaurantapi.model.OTP;
import org.edu.restaurantapi.model.User;
import org.edu.restaurantapi.repository.UserRepository;
import org.edu.restaurantapi.request.EmailRequest;
import org.edu.restaurantapi.repository.OtpRepository;
import org.edu.restaurantapi.request.ResetPasswordRequest;
import org.edu.restaurantapi.util.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class EmailService {

    @Value("${email.owner}")
    private String emailOwner;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private UserRepository userRepository;

    public void sendEmailWithOtp(User user) {
        String otpCode = generateOtp(); // Tạo mã OTP
        OTP otp = new OTP(); // Tạo một đối tượng OTP mới
        otp.setOtpCode(otpCode); // Đặt mã OTP
        otp.setUser(user); // Thiết lập người dùng
        otp.setCreatedAt(LocalDateTime.now()); // Thời gian tạo mã OTP
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(5)); // Hết hạn sau 5 phút
        otpRepository.save(otp); // Lưu OTP vào cơ sở dữ liệu

        // Gửi email với mã OTP
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setTo(user.getEmail()); // Địa chỉ email người nhận
        emailRequest.setSubject("Mã OTP xác thực"); // Tiêu đề email
        emailRequest.setText("Mã OTP của bạn là: " + otpCode); // Nội dung email
        sendEmail(emailRequest); // Gọi phương thức gửi email
    }

    public void sendEmail(EmailRequest emailRequest) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailRequest.getTo());
            message.setSubject(emailRequest.getSubject());
            message.setText(emailRequest.getText());
            message.setFrom(emailOwner);
            mailSender.send(message);
        } catch (MailAuthenticationException e) {
            throw new RuntimeException("Looi ne: " + e.getMessage());
        } catch (MailException e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }

    public void sendHtmlEmail(EmailRequest emailRequest, String htmlContent) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setText(htmlContent, true); // true để chỉ định đây là email HTML
        helper.setTo(emailRequest.getTo());
        helper.setSubject(emailRequest.getSubject());
        helper.setFrom(emailOwner);
        mailSender.send(mimeMessage);
    }

    public Boolean resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByEmailAndIsDeleteFalse(request.getEmail()).orElse(null);
        if (user != null) {
            String hashedPassword = PasswordUtil.hashPassword(request.getNewPassword());
            user.setPassword(hashedPassword);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public String generateOtp() {
        SecureRandom secureRandom = new SecureRandom();
        return String.format("%06d", secureRandom.nextInt(1000000));
    }

    public OTP findLatestOtpByUserId(Long id) {
        return otpRepository.findLatestOtpByUserId(id);
    }
}
