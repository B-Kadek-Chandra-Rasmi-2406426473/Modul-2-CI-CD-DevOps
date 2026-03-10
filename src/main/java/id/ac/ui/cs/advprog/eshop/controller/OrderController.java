package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/create")
    public String createOrderPage() {
        return "CreateOrder";
    }

    @PostMapping("/create")
    public String createOrderPost(@RequestParam("author") String author) {
        List<Product> products = new ArrayList<>();
        Product dummyProduct = new Product();
        dummyProduct.setProductId("dummy-id");
        dummyProduct.setProductName("Dummy Product");
        dummyProduct.setProductQuantity(1);
        products.add(dummyProduct);

        Order newOrder = new Order(UUID.randomUUID().toString(), products, System.currentTimeMillis(), author);
        orderService.createOrder(newOrder);

        return "redirect:/order/history";
    }

    @GetMapping("/history")
    public String historyOrderPage() {
        return "OrderHistory";
    }

    @PostMapping("/history")
    public String historyOrderPost(@RequestParam("author") String author, Model model) {
        List<Order> orders = orderService.findAllByAuthor(author);
        model.addAttribute("orders", orders);
        model.addAttribute("author", author);
        return "OrderList";
    }

    @GetMapping("/pay/{orderId}")
    public String payOrderPage(@PathVariable("orderId") String orderId, Model model) {
        Order order = orderService.findById(orderId);
        model.addAttribute("order", order);
        return "PayOrder";
    }

    @PostMapping("/pay/{orderId}")
    public String payOrderPost(@PathVariable("orderId") String orderId,
                               @RequestParam("method") String method,
                               @RequestParam(value = "data1", required = false) String data1,
                               @RequestParam(value = "data2", required = false) String data2,
                               Model model) {
        return "redirect:/order/history";
    }
}