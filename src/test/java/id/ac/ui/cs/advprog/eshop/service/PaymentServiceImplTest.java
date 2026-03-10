package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @InjectMocks
    PaymentServiceImpl paymentService;

    @Mock
    PaymentRepository paymentRepository;

    List<Payment> payments;
    Order order;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(2);
        products.add(product1);

        order = new Order("13652556-012a-4c07-b546-54eb1396d79b",
                products, 1708560000L, "Safira Sudrajat");

        payments = new ArrayList<>();
        Map<String, String> paymentData1 = new HashMap<>();
        paymentData1.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment1 = new Payment("payment-1", order, "VOUCHER", paymentData1);
        payments.add(payment1);

        Map<String, String> paymentData2 = new HashMap<>();
        paymentData2.put("address", "Jalan Margonda");
        paymentData2.put("deliveryFee", "10000");
        Payment payment2 = new Payment("payment-2", order, "CASH_ON_DELIVERY", paymentData2);
        payments.add(payment2);
    }

    @Test
    void testAddPayment() {
        Payment payment = payments.get(0);
        doReturn(payment).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.addPayment(payment.getOrder(), payment.getMethod(), payment.getPaymentData());

        verify(paymentRepository, times(1)).save(any(Payment.class));
        assertEquals(payment.getId(), result.getId());
    }

    @Test
    void testSetStatusSuccess() {
        Payment payment = payments.get(0);
        doReturn(payment).when(paymentRepository).save(payment);

        Payment result = paymentService.setStatus(payment, "SUCCESS");

        assertEquals("SUCCESS", result.getStatus());
        assertEquals("SUCCESS", result.getOrder().getStatus());
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testSetStatusRejected() {
        Payment payment = payments.get(1);
        doReturn(payment).when(paymentRepository).save(payment);

        Payment result = paymentService.setStatus(payment, "REJECTED");

        assertEquals("REJECTED", result.getStatus());
        assertEquals("FAILED", result.getOrder().getStatus());
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testSetStatusInvalid() {
        Payment payment = payments.get(0);
        assertThrows(IllegalArgumentException.class, () -> {
            paymentService.setStatus(payment, "INVALID_STATUS");
        });
        verify(paymentRepository, times(0)).save(payment);
    }

    @Test
    void testGetPaymentIfFound() {
        Payment payment = payments.get(0);
        doReturn(payment).when(paymentRepository).findById(payment.getId());

        Payment result = paymentService.getPayment(payment.getId());
        assertEquals(payment.getId(), result.getId());
    }

    @Test
    void testGetPaymentIfNotFound() {
        doReturn(null).when(paymentRepository).findById("invalid-id");
        assertNull(paymentService.getPayment("invalid-id"));
    }

    @Test
    void testGetAllPayments() {
        doReturn(payments).when(paymentRepository).findAll();
        List<Payment> result = paymentService.getAllPayments();
        assertEquals(2, result.size());
    }
}