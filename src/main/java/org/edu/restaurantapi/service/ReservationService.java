package org.edu.restaurantapi.service;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.edu.restaurantapi._enum.OrderStatus;
import org.edu.restaurantapi.model.Branch;
import org.edu.restaurantapi.model.Order;
import org.edu.restaurantapi.model.Reservation;
import org.edu.restaurantapi.model.Table;
import org.edu.restaurantapi.repository.BranchRepository;
import org.edu.restaurantapi.repository.OrderRepository;
import org.edu.restaurantapi.repository.ReservationRepository;
import org.edu.restaurantapi.repository.TableRepository;
import org.edu.restaurantapi.request.EmailRequest;
import org.edu.restaurantapi.request.ReserTableCancelRequest;
import org.edu.restaurantapi.request.ReserTableOffLineRequest;
import org.edu.restaurantapi.request.ReservationOnlineRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private OrderRepository orderRepository;

    public List<Reservation> getAllReservations(String branch) {
        return reservationRepository.findByBranchId(Long.parseLong(branch));
    }

    public Reservation createReservation(ReservationOnlineRequest request) throws MessagingException {
        Branch branch = branchRepository.findById(request.getBranchId()).orElse(null);
        List<Table> tables = tableRepository.findAllById(List.of(request.getTableIds()));
        List<Reservation> reservations = tables.stream()
                .map(table -> Reservation.builder()
                        .branch(branch)
                        .table(table)
                        .fullName(request.getFullName())
                        .email(request.getEmail())
                        .phoneNumber(request.getPhoneNumber())
                        .bookingDate(request.getBookingDate())
                        .startTime(request.getStartTime())
                        .notes(request.getNotes())
                        .build())
                .collect(Collectors.toList());

        reservationRepository.saveAll(reservations);
        return reservations.isEmpty() ? null : reservations.get(0);
    }

    private void sendConfirm(Reservation request) throws MessagingException {
        String branchName = request.getBranch() != null ? request.getBranch().getName() : "Chưa cập nhật";
        String tableInfo = request.getTable() != null ? "Bàn " + request.getTable().getNumber() : "Chưa cập nhật";

        // Nội dung email
        String content = "<!DOCTYPE html>" +
                "<html lang='vi'>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "    <style>" +
                "        body { font-family: Arial, sans-serif; color: #333333; padding: 20px; }" +
                "        .container { max-width: 600px; margin: auto; border: 1px solid #dddddd; border-radius: 8px; overflow: hidden; box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1); }" +
                "        .header { background-color: #595653; color: #ffffff; padding: 15px 10px; text-align: center; font-size: 1.5em; }" +
                "        .content { padding: 20px; }" +
                "        .card { border: 1px solid #dddddd; border-radius: 8px; margin-bottom: 15px; padding: 15px; background-color: #f9f9f9; }" +
                "        .card h4 { margin: 0; font-size: 1.2em; color: #333333; }" +
                "        .card p { margin: 5px 0; font-size: 0.95em; color: #555555; }" +
                "        .footer { text-align: center; padding: 10px; font-size: 0.9em; color: #888888; background-color: #f1f1f1; border-top: 1px solid #dddddd; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class='container'>" +
                "        <div class='header'>Xác nhận đặt bàn thành công</div>" +
                "        <div class='content'>" +
                "            <p>Kính gửi Quý khách <strong>" + request.getFullName() + "</strong>,</p>" +
                "            <p>Cảm ơn Quý khách đã đặt bàn tại Nhà hàng <strong>FooDY</strong>. Dưới đây là thông tin chi tiết:</p>" +
                "            <div class='card'>" +
                "                <h4>Thông tin khách hàng</h4>" +
                "                <p><strong>Họ và tên:</strong> " + request.getFullName() + "</p>" +
                "                <p><strong>Email:</strong> " + (request.getEmail() != null ? request.getEmail() : "Chưa cập nhật") + "</p>" +
                "                <p><strong>Số điện thoại:</strong> " + (request.getPhoneNumber() != null ? request.getPhoneNumber() : "Chưa cập nhật") + "</p>" +
                "            </div>" +
                "            <div class='card'>" +
                "                <h4>Thông tin đặt bàn</h4>" +
                "                <p><strong>Chi nhánh:</strong> " + branchName + "</p>" +
                "                <p><strong>Bàn:</strong> " + tableInfo + "</p>" +
                "                <p><strong>Ngày đặt:</strong> " + request.getBookingDate() + "</p>" +
                "                <p><strong>Thời gian:</strong> " + request.getStartTime() + (request.getEndTime() != null ? " - " + request.getEndTime() : "") + "</p>" +
                "                <p><strong>Yêu cầu đặc biệt:</strong> " + (request.getNotes() != null ? request.getNotes() : "Không có") + "</p>" +
                "            </div>" +
                "            <p>Vui lòng đến sớm 10 phút trước giờ đặt để được phục vụ tốt nhất.</p>" +
                "            <p>Nếu có bất kỳ câu hỏi nào, Quý khách vui lòng liên hệ với chúng tôi qua hotline: <strong>1800-1234-567</strong>.</p>" +
                "        </div>" +
                "        <div class='footer'>" +
                "            <p>Trân trọng,</p>" +
                "            <p><strong>FooDY</strong></p>" +
                "            <p>Địa chỉ chi nhánh: [Địa chỉ Chi nhánh]</p>" +
                "            <p>Hotline: 1800-1234-567</p>" +
                "            <p>Email: <a href='mailto:[email@example.com]'>[email@example.com]</a></p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";

        // Gửi email
        EmailRequest emailRequest = EmailRequest
                .builder()
                .to(request.getEmail())
                .subject("Xác nhận đặt bàn thành công tại Nhà hàng FooDY")
                .build();
        emailService.sendHtmlEmailAsync(emailRequest, content);
    }

    public Reservation cancelReservation(ReserTableCancelRequest request) {
        List<Reservation> reservations = reservationRepository.findAllById(Arrays.asList(request.getReservationIds()));
        reservations.forEach(r -> {
            r.setCancelReason(request.getReason());
            r.setIsDelete(true);
            reservationRepository.save(r);
        });
        return reservations.isEmpty() ? null : reservations.get(0);
    }

    private void sendCancel(Long reservationId, String reason) throws MessagingException {
        Optional<Reservation> reservation = reservationRepository.findById(reservationId);
        // Nội dung email
        String content = "<!DOCTYPE html>" +
                "<html lang='vi'>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "    <style>" +
                "        body { font-family: Arial, sans-serif; color: #333333; padding: 20px; }" +
                "        .container { max-width: 600px; margin: auto; border: 1px solid #dddddd; border-radius: 8px; overflow: hidden; box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1); }" +
                "        .header { background-color: #D9534F; color: #ffffff; padding: 15px 10px; text-align: center; font-size: 1.5em; }" +
                "        .content { padding: 20px; }" +
                "        .card { border: 1px solid #dddddd; border-radius: 8px; margin-bottom: 15px; padding: 15px; background-color: #f9f9f9; }" +
                "        .card h4 { margin: 0; font-size: 1.2em; color: #333333; }" +
                "        .card p { margin: 5px 0; font-size: 0.95em; color: #555555; }" +
                "        .footer { text-align: center; padding: 10px; font-size: 0.9em; color: #888888; background-color: #f1f1f1; border-top: 1px solid #dddddd; }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class='container'>" +
                "        <div class='header'>Thông báo hủy đặt bàn</div>" +
                "        <div class='content'>" +
                "            <p>Kính gửi Quý khách <strong>" + reservation.get().getFullName() + "</strong>,</p>" +
                "            <p>Chúng tôi rất tiếc phải thông báo rằng đơn đặt bàn của Quý khách tại Nhà hàng <strong>FooDY</strong> đã bị hủy. Dưới đây là thông tin chi tiết:</p>" +
                "            <div class='card'>" +
                "                <h4>Thông tin khách hàng</h4>" +
                "                <p><strong>Họ và tên:</strong> " + reservation.get().getFullName() + "</p>" +
                "                <p><strong>Email:</strong> " + (reservation.get().getEmail() != null ? reservation.get().getEmail() : "Chưa cập nhật") + "</p>" +
                "                <p><strong>Số điện thoại:</strong> " + (reservation.get().getPhoneNumber() != null ? reservation.get().getPhoneNumber() : "Chưa cập nhật") + "</p>" +
                "            </div>" +
                "            <div class='card'>" +
                "                <h4>Thông tin đặt bàn</h4>" +
                "                <p><strong>Chi nhánh:</strong> " + reservation.get().getBranch().getName() + "</p>" +
                "                <p><strong>Bàn:</strong> " + reservation.get().getTable().getNumber() + "</p>" +
                "                <p><strong>Ngày đặt:</strong> " + reservation.get().getBookingDate() + "</p>" +
                "                <p><strong>Thời gian:</strong> " + reservation.get().getStartTime() + (reservation.get().getEndTime() != null ? " - " + reservation.get().getEndTime() : "") + "</p>" +
                "            </div>" +
                "            <p><strong>Lý do hủy:</strong> " + reason + "</p>" +
                "            <p>Chúng tôi rất tiếc về sự bất tiện này và hy vọng sẽ có cơ hội phục vụ Quý khách trong những lần đặt bàn tiếp theo.</p>" +
                "            <p>Nếu có bất kỳ câu hỏi nào, Quý khách vui lòng liên hệ với chúng tôi qua hotline: <strong>1800-1234-567</strong>.</p>" +
                "        </div>" +
                "        <div class='footer'>" +
                "            <p>Trân trọng,</p>" +
                "            <p><strong>FooDY</strong></p>" +
                "            <p>Địa chỉ chi nhánh: [Địa chỉ Chi nhánh]</p>" +
                "            <p>Hotline: 1800-1234-567</p>" +
                "            <p>Email: <a href='mailto:[email@example.com]'>[email@example.com]</a></p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";

        // Gửi email
        EmailRequest emailRequest = EmailRequest
                .builder()
                .to(reservation.get().getEmail())
                .subject("Thông báo hủy đặt bàn tại Nhà hàng FooDY")
                .build();
        emailService.sendHtmlEmail(emailRequest, content);
    }

    public Page<Reservation> getAllCancelReservations(String branch, Pageable pageable) {
        return reservationRepository.findByBranchIdAndCancelReasonIsNotNull(Long.parseLong(branch), pageable);
    }

    public Reservation createReservationOffline(ReserTableOffLineRequest request) {
        Branch branch = branchRepository.findById(request.getBranchId()).orElse(null);
        List<Table> tables = tableRepository.findAllById(List.of(request.getTableIds()));

        List<Reservation> reservations = tables.stream()
                .map(table -> {
                    // Tạo reservation
                    Order order = Order.builder()
                            .orderStatus(OrderStatus.READY_TO_SERVE)
                            .branch(branch)
                            .table(table)
                            .fullName(request.getFullName())
                            .phoneNumber(request.getPhoneNumber())
                            .build();

                    orderRepository.save(order);

                    Reservation reservation = Reservation.builder()
                            .branch(branch)
                            .table(table)
                            .fullName(request.getFullName())
                            .phoneNumber(request.getPhoneNumber())
                            .bookingDate(request.getBookingDate())
                            .startTime(request.getStartTime())
                            .order(order)
                            .build();

                    return reservation;
                })
                .collect(Collectors.toList());

        reservationRepository.saveAll(reservations);

        return reservations.isEmpty() ? null : reservations.get(0);
    }

}
