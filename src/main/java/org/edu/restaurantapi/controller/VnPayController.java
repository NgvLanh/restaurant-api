package org.edu.restaurantapi.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.edu.restaurantapi.request.VnPayRequest;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.response.VnPayResponse;
import org.edu.restaurantapi.response.VnPayReturnResponse;
import org.edu.restaurantapi.service.VnPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vnpay")
public class VnPayController {

    @Autowired
    private VnPayService vnPayService;

    @PostMapping
    private ResponseEntity<?> createVnPayPayment(@RequestBody @Valid VnPayRequest vnPayRequest) {
        String response = vnPayService.createVnPayPayment(vnPayRequest);
        return ResponseEntity.ok()
                .body(new ApiResponse<>(
                        200, true, "VnPay created payment success",
                        new VnPayResponse(response)));
    }

    @GetMapping("/return")
    private ResponseEntity<?> returnVnPayPayment(HttpServletRequest request) {
        String orderId = request.getParameter("vnp_OrderInfo");
        Integer response = vnPayService.vnPayPaymentReturn(request);
        if (response == 0) {
//            return "redirect:/success-page?orderId=" + orderId;
            return ResponseEntity.ok()
                    .body(ApiResponse.SUCCESS(null));
        } else if (response == 1){
            // khi thất bại cần gọi lại hàm xoá hoá đơn để xoá hoá đơn đã tạo
            // ...
//            return "redirect:/cancel-page?orderId=" + orderId;
            return ResponseEntity.status(202) // cancel
                    .body(ApiResponse.CANCEL(null));
        } else {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.SERVER_ERROR("Lỗi chữ ký"));
        }
    }
}
