package org.edu.restaurantapi.service;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.edu.restaurantapi.model.Reservation;
import org.edu.restaurantapi.model.TableReservation;
import org.edu.restaurantapi.model.User;
import org.edu.restaurantapi.repository.BranchRepository;
import org.edu.restaurantapi.repository.ReservationRepository;
import org.edu.restaurantapi.request.EmailRequest;
import org.edu.restaurantapi.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
public class ReservationService {
    @Autowired
    private ReservationRepository repository;

    @Autowired
    private TableReservationService tableReservationService;

    @Autowired
    private EmailService emailService;

    public Reservation create(Reservation request) throws MessagingException {
        sendConfirm(request);
        Boolean conflict = repository.isTableReserved(request.getTable().getId(),
                request.getBookingDate(), request.getStartTime());
        var response = repository.save(request);
        if (!conflict) {
            response.setIsConflict(false);
            tableReservationService.create(TableReservation.builder()
                    .reservation(response)
                    .createDate(LocalDate.now())
                    .isDelete(false)
                    .build());
            return response;
        } else {
            response.setIsConflict(true);
            return repository.save(response);
        }
    }

    private void sendConfirm(Reservation request) throws MessagingException {
        String content = "<!DOCTYPE html>" +
                "<html lang='vi'>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "    <style>" +
                "        body { font-family: Arial, sans-serif; color: #333333; padding: 20px; }" +
                "        .container { max-width: 600px; margin: auto; border: 1px solid #dddddd; border-radius: 8px; overflow: hidden; }" +
                "        .header { background-color: #595653; color: #ffffff; padding: 15px 10px; text-align: center; font-size: 1.5em; }" +
                "        .content { padding: 20px; }" +
                "        .footer { text-align: center; padding: 10px; font-size: 0.9em; color: #888888; }" +
                "        .table { width: 100%; margin-top: 20px; border-collapse: collapse; }" +
                "        .table td { padding: 8px; border: 1px solid #dddddd; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class='container'>" +
                "        <div class='header'>Xác nhận đặt bàn thành công</div>" +
                "        <div class='content'>" +
                "            <p>Kính gửi Quý khách <strong>" + request.getFullName() + "</strong>,</p>" +
                "            <p>Cảm ơn Quý khách đã đặt bàn tại Nhà hàng <strong>[Tên Nhà hàng]</strong>. Chúng tôi xin gửi đến Quý khách thông tin chi tiết về đặt bàn như sau:</p>" +
                "            <table class='table'>" +
                "                <tr><td><strong>Họ và tên:</strong></td><td>" + request.getFullName() + "</td></tr>" +
                "                <tr><td><strong>Số điện thoại:</strong></td><td>" + (request.getPhoneNumber() == null ? "Chưa cập nhật" : request.getPhoneNumber()) + "</td></tr>" +
                "                <tr><td><strong>Chi nhánh:</strong></td><td>" + request.getBranch().getName() + "</td></tr>" +
                "                <tr><td><strong>Số bàn/ghế:</strong></td><td>" + request.getTable().getNumber() + " / " + request.getTable().getSeats() + "</td></tr>" +
                "                <tr><td><strong>Khu vực:</strong></td><td>" + request.getTable().getZone().getName() + "</td></tr>" +
                "                <tr><td><strong>Thời gian:</strong></td><td>Từ " + request.getStartTime() + " đến " + request.getEndTime() + "</td></tr>" +
                "                <tr><td><strong>Ngày đặt:</strong></td><td>" + request.getBookingDate() + "</td></tr>" +
                "                <tr><td><strong>Yêu cầu đặc biệt:</strong></td><td>" + (request.getNotes() != null ? request.getNotes() : "Không có") + "</td></tr>" +
                "            </table>" +
                "            <p>Vui lòng đến sớm 10 phút trước giờ đặt để được phục vụ tốt nhất.</p>" +
                "            <p>Nếu có bất kỳ câu hỏi nào, Quý khách vui lòng liên hệ với chúng tôi qua hotline: <strong>1800-1234-567</strong>.</p>" +
                "        </div>" +
                "        <div class='footer'>" +
                "            <p>Trân trọng,</p>" +
                "            <p><strong>[Tên Nhà hàng]</strong></p>" +
                "            <p>Địa chỉ chi nhánh: [Địa chỉ Chi nhánh]</p>" +
                "            <p>Hotline: 1800-1234-567</p>" +
                "            <p>Email: <a href='mailto:[email@example.com]'>[email@example.com]</a></p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";

        EmailRequest emailRequest = EmailRequest
                .builder()
                .to(request.getEmail())
                .subject("Xác nhận đặt bàn thành công tại Nhà hàng [Tên Nhà hàng]")
                .build();
        emailService.sendHtmlEmail(emailRequest, content);
    }

    public Page<Reservation> gets(String branch, Pageable pageable) {
        return repository.findByBranchIdAndIsConflictTrue(Long.parseLong(branch), pageable);
    }
}
