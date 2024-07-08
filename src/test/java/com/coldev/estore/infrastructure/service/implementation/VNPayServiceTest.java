package com.coldev.estore.infrastructure.service.implementation;

import com.coldev.estore.infrastructure.service.implementation.VNPayServiceImpl;
import com.coldev.estore.domain.entity.CustomerOrder;
import com.coldev.estore.config.exception.vnpay.VNPayException;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@Log4j2
public class VNPayServiceTest {

    private VNPayServiceImpl vnpayService;

    @BeforeEach
    void setUp() {
        vnpayService = new VNPayServiceImpl();
    }

    @Test
    void testGeneratePaymentLink() {
        // Mock CustomerOrder
        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setId(123456L); // Example order ID
        customerOrder.setNetAmount(BigDecimal.valueOf(1000000000)); // Example net amount

        // Generate payment link
        String paymentLink = vnpayService.generatePaymentLink(customerOrder);

        log.info("PAYMENT LINK: {}", paymentLink);

        // Assert that payment link is not null and contains expected parts
        assertNotNull(paymentLink);
        assertTrue(paymentLink.startsWith("https://sandbox.vnpayment.vn/paymentv2/vpcpay.html"));
        assertTrue(paymentLink.contains("vnp_TmnCode=3B9772GY")); // Check TMN code
        assertTrue(paymentLink.contains("vnp_ReturnUrl=http%3A%2F%2Flocalhost%3A9999%2Fapi%2Fv1%2Fpayments%2Fpayment%2Fcall-back")); // Check Return URL
        assertTrue(paymentLink.contains("vnp_SecureHash=")); // Ensure secure hash is included

        // Add more specific checks depending on your actual implementation and expected behavior
    }

    @Test
    void testGeneratePaymentLinkWithInvalidData() {
        // Test with invalid data scenario (if applicable)
        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setId(null); // Invalid order ID

        // Assert that VNPayException is thrown
        VNPayException exception = assertThrows(VNPayException.class, () -> {
            vnpayService.generatePaymentLink(customerOrder);
        });

        assertEquals("VNPAY_GOT_PROBLEM", exception.getMessage()); // Check the exception message
    }
}
