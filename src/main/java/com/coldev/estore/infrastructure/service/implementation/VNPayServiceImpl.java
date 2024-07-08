package com.coldev.estore.infrastructure.service.implementation;

import com.coldev.estore.common.constant.MessageDictionary;
import com.coldev.estore.config.exception.vnpay.VNPayException;
import com.coldev.estore.domain.entity.CustomerOrder;
import com.coldev.estore.domain.service.VNPAYService;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VNPayServiceImpl implements VNPAYService {

    // VNPAY configuration parameters
    private static final String VNPAY_URL = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    //private static final String VNPAY_TMNCODE = "YOUR_TMNCODE";
    private static final String VNPAY_TMNCODE = "3B9772GY";
    //private static final String VNPAY_HASHSECRET = "YOUR_HASHSECRET";
    private static final String VNPAY_HASHSECRET = "YOWQLHSQHCLHDRPOIMLACFRGVQZTLUPM";
    private static final String VNPAY_RETURNURL = "http://localhost:9999/api/v1/payments/payment/call-back";

    public static String generatePaymentLink(CustomerOrder customerOrder) {
        try {
            // Construct the parameter map
            Map<String, String> params = new HashMap<>();
            params.put("vnp_Version", "2.1.0");
            params.put("vnp_Command", "pay");
            params.put("vnp_TmnCode", VNPAY_TMNCODE);
            params.put("vnp_Amount", String.valueOf(customerOrder.getNetAmount().multiply(BigDecimal.valueOf(1000)).intValue())); // Amount in VND
            params.put("vnp_CurrCode", "VND");
            params.put("vnp_TxnRef", String.valueOf(customerOrder.getId()));
            params.put("vnp_OrderInfo", "Payment for order: " + customerOrder.getId());
            params.put("vnp_Locale", "vn");
            params.put("vnp_ReturnUrl", VNPAY_RETURNURL);
            params.put("vnp_CreateDate", getCurrentTime());

            // Generate the hash
            String queryString = params.entrySet().stream()
                    .map(entry -> URLEncoder.encode(entry.getKey(), StandardCharsets.US_ASCII) + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII))
                    .collect(Collectors.joining("&"));

            String secureHash = hmacSHA512(VNPAY_HASHSECRET, queryString);
            params.put("vnp_SecureHash", secureHash);

            // Construct the payment link
            String paymentLink = VNPAY_URL + "?" + params.entrySet().stream()
                    .map(entry -> URLEncoder.encode(entry.getKey(), StandardCharsets.US_ASCII) + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII))
                    .collect(Collectors.joining("&"));

            return paymentLink;
        } catch (Exception e) {
            // Handle exception (logging, rethrowing, etc.)
            throw new VNPayException(MessageDictionary.VNPAY_GOT_PROBLEM);
        }
    }

    private static String getCurrentTime() {
        // Generate current time in the required format: yyyyMMddHHmmss
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return LocalDateTime.now().format(formatter);
    }

    private static String hmacSHA512(String key, String data) throws NoSuchAlgorithmException {
        try {
            // Create HMAC SHA512 hash
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA512");
            Mac mac = Mac.getInstance("HmacSHA512");
            mac.init(secretKeySpec);
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate HMAC SHA512", e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}
