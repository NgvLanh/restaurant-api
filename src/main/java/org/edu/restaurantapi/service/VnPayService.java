package org.edu.restaurantapi.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.edu.restaurantapi.repository.OrderRepository;
import org.edu.restaurantapi.request.VnPayRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VnPayService {

    @Value("${VNP.vnp_TmnCode}")
    private String vnp_TmnCode;

    @Value("${VNP.vnp_HashSecret}")
    private String vnp_HashSecret;

    @Value("${VNP.vnp_Url}")
    private String vnp_Url;

    @Value("${vnp.version:2.1.0}")
    private String vnpVersion;

    @Value("${vnp.command:pay}")
    private String vnpCommand;

    @Value("${vnp.locale:vn}")
    private String vnpLocale;

    @Value("${vnp.orderType:order-type}")
    private String orderType;

    private final String returnUrl = "http://localhost:8080/api/vnpay/return";

    // * 1 số tham số cần chú ý
    // vnp_Amount - giá tiền hoá đơn
    // vnp_OrderInfo - thông tin hoá đơn - ở đây truyền vào là id hoá đơn (khi huỷ thì xoá hoá đơn đã tạo)
    // vnp_IpAddr - địa chỉ Ip của người mua - ở đây truyền là id của người thanh toán

    public String createVnPayPayment(VnPayRequest vnPayRequest) {
        String vnpTxnRef = getRandomNumber(8);
        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_Version", vnpVersion);
        vnpParams.put("vnp_Command", vnpCommand);
        vnpParams.put("vnp_TmnCode", vnp_TmnCode);
        vnpParams.put("vnp_CurrCode", "VND");
        vnpParams.put("vnp_TxnRef", vnpTxnRef);
        vnpParams.put("vnp_OrderType", orderType);
        vnpParams.put("vnp_Locale", vnpLocale);
        vnpParams.put("vnp_ReturnUrl", returnUrl);
        // Tham số cần chú ý
        vnpParams.put("vnp_IpAddr", vnPayRequest.getUserId().toString());
        vnpParams.put("vnp_OrderInfo", vnPayRequest.getOrderId().toString());
        vnpParams.put("vnp_Amount", String.valueOf(vnPayRequest.getAmount() * 100));

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnpCreateDate = formatter.format(cld.getTime());
        vnpParams.put("vnp_CreateDate", vnpCreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnpExpireDate = formatter.format(cld.getTime());
        vnpParams.put("vnp_ExpireDate", vnpExpireDate);

        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (String fieldName : fieldNames) {
            String fieldValue = vnpParams.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                try {
                    hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()))
                            .append('=')
                            .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (!fieldName.equals(fieldNames.get(fieldNames.size() - 1))) {
                        query.append('&');
                        hashData.append('&');
                    }
                } catch (UnsupportedEncodingException e) {
                    log.error("Error encoding URL parameters", e);
                }
            }
        }

        String vnpSecureHash = hmacSHA512(vnp_HashSecret, hashData.toString());
        query.append("&vnp_SecureHash=").append(vnpSecureHash);

        return vnp_Url + "?" + query;
    }

    public static String hmacSHA512(final String key, final String data) {
        try {
            if (key == null || data == null) {
                throw new NullPointerException();
            }
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac512.init(secretKey);
            byte[] result = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Exception ex) {
            return "";
        }
    }

    public String getRandomNumber(int len) {
        Random rnd = new Random();
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public Integer vnPayPaymentReturn(HttpServletRequest request) {
        Map<String, String> vnp_Params = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements(); ) {
            String paramName = params.nextElement();
            vnp_Params.put(paramName, request.getParameter(paramName));
        }
        String vnp_SecureHash = vnp_Params.remove("vnp_SecureHash");
        String hashData = vnp_Params.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        String secureHash = hmacSHA512(vnp_HashSecret, hashData);

        if (secureHash.equals(vnp_SecureHash)) {
            // Kiểm tra trạng thái thanh toán và xử lý đơn hàng
            String responseCode = vnp_Params.get("vnp_ResponseCode");
            if ("00".equals(responseCode)) {
                return 0; // Thành công
            } else {
                return 1; // Thất bại - huỷ khi thanh toán
            }
        } else {
            return null; // Thất bại - lỗi chữ ký (vnp_HashSecret) sai
        }
    }
}
