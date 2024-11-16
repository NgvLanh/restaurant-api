package org.edu.restaurantapi.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.edu.restaurantapi.model.Cart;
import org.edu.restaurantapi.model.Order;
import org.edu.restaurantapi.model.User;
import org.edu.restaurantapi.request.VnPayRequest;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.response.VnPayResponse;
import org.edu.restaurantapi.response.VnPayReturnResponse;
import org.edu.restaurantapi.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/vnpay")
public class VnPayController {

    @Autowired
    private VnPayService vnPayService;

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private CartService cartService;

    @PostMapping
    private ResponseEntity<?> createVnPayPayment(@RequestBody @Valid VnPayRequest vnPayRequest) {
        String response = vnPayService.createVnPayPayment(vnPayRequest);
        return ResponseEntity.ok()
                .body(new ApiResponse<>(
                        200, true, "Tạo hoá đơn VnPay thành công",
                        new VnPayResponse(response)));
    }

    @GetMapping("/return")
    private ResponseEntity<?> returnVnPayPayment(HttpServletRequest request) {
        String orderId = request.getParameter("vnp_OrderInfo");
        String userId = request.getParameter("vnp_IpAddr");
        String Amount = request.getParameter("vnp_Amount");
        Map<String, String> map = new HashMap<>();
        map.put("OrderId", orderId);
        map.put("UserId", userId);
        map.put("Amount", Amount);
        Integer response = vnPayService.vnPayPaymentReturn(request);
        Optional<Order> order = orderService.findById(Long.parseLong(orderId));
        Optional<Cart> cart = cartService.findByCartUserId(order.get().getUser().getId());
        if (response == 0) {
            cartItemService.deleteCartItemsByCartId(cart.get().getId());
            // Trả về response với mã 302 (Found) kèm URL chuyển hướng
            return ResponseEntity.status(302)
                    .header("Location", "http://localhost:4000/home")
                    .build();
        } else if (response == 1) {
            orderService.deleteOrder(orderId);
            return ResponseEntity.status(302)
                    .header("Location", "http://localhost:4000/checkout")
                    .build();
        } else {
            return ResponseEntity.status(302)
                    .header("Location", "/home?error=signature")
                    .build();
        }
    }
}
