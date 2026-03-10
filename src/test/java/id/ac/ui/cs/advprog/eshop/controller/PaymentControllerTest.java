package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @MockBean
    private OrderService orderService;

    private Order order;
    private Payment payment;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        order = new Order("order-123", products, 1708560000L, "Chandra");
        payment = new Payment("payment-123", order, "VOUCHER", new HashMap<>());
    }

    @Test
    void testCreatePaymentPost() throws Exception {
        Mockito.when(orderService.findById(anyString())).thenReturn(order);
        Mockito.when(paymentService.addPayment(any(), anyString(), any())).thenReturn(payment);
        Mockito.when(paymentService.setStatus(any(), anyString())).thenReturn(payment);

        mockMvc.perform(post("/payment/create")
                        .param("orderId", "order-123")
                        .param("method", "VOUCHER")
                        .param("data1", "ESHOP1234ABC5678"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/payment/payment-123"));
    }

    @Test
    void testGetPaymentReceiptPage() throws Exception {
        Mockito.when(paymentService.getPayment("payment-123")).thenReturn(payment);

        mockMvc.perform(get("/payment/payment-123"))
                .andExpect(status().isOk())
                .andExpect(view().name("PaymentReceipt"))
                .andExpect(model().attributeExists("payment"));
    }
}