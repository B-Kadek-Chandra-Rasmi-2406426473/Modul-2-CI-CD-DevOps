package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;
import java.util.Map;

@Getter
public class Payment {
    String id;
    String method;
    String status;
    Map<String, String> paymentData;
    Order order;

    public Payment(String id, Order order, String method, Map<String, String> paymentData) {
        this.id = id;
        this.order = order;
        this.method = method;
        this.paymentData = paymentData;

        if ("VOUCHER".equals(method)) {
            this.status = validateVoucher() ? "SUCCESS" : "REJECTED";
        } else if ("CASH_ON_DELIVERY".equals(method)) {
            this.status = validateCod() ? "SUCCESS" : "REJECTED";
        } else {
            this.status = "REJECTED";
        }
    }

    private boolean validateVoucher() {
        String voucher = paymentData.get("voucherCode");
        return voucher != null && voucher.length() == 16 &&
                voucher.startsWith("ESHOP") &&
                voucher.chars().filter(Character::isDigit).count() == 8;
    }

    private boolean validateCod() {
        String address = paymentData.get("address");
        String deliveryFee = paymentData.get("deliveryFee");
        return address != null && !address.isEmpty() &&
                deliveryFee != null && !deliveryFee.isEmpty();
    }
}