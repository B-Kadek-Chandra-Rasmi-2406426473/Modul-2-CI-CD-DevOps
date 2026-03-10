package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public String createPayment(@RequestParam("orderId") String orderId,
                                @RequestParam("method") String method,
                                @RequestParam(value = "data1", required = false) String data1,
                                @RequestParam(value = "data2", required = false) String data2) {

        Order order = orderService.findById(orderId);
        Map<String, String> paymentData = new HashMap<>();

        if ("VOUCHER".equals(method)) {
            paymentData.put("voucherCode", data1);
        } else if ("CASH_ON_DELIVERY".equals(method)) {
            paymentData.put("address", data1);
            paymentData.put("deliveryFee", data2);
        }
        Payment payment = paymentService.addPayment(order, method, paymentData);
        paymentService.setStatus(payment, payment.getStatus());

        return "redirect:/payment/" + payment.getId();
    }

    @GetMapping("/{paymentId}")
    public String paymentReceiptPage(@PathVariable("paymentId") String paymentId, Model model) {
        Payment payment = paymentService.getPayment(paymentId);
        model.addAttribute("payment", payment);
        return "PaymentReceipt";
    }
}