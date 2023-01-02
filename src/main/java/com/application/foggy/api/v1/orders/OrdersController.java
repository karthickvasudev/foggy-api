package com.application.foggy.api.v1.orders;

import com.application.foggy.api.v1.orders.modals.CreateOrder;
import com.application.foggy.api.v1.orders.modals.MakePayment;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/orders")
@AllArgsConstructor
public class OrdersController {
    private final OrdersService service;

    @PostMapping("create")
    public Order create(@RequestBody CreateOrder order) {
        return service.createAnOrder(order);
    }

    @GetMapping("{id}")
    public Order get(@PathVariable String id) {
        return service.getOrder(id);
    }

    @GetMapping()
    public List<Order> list() {
        return service.getAllOrders();
    }

    @PutMapping("{id}")
    public Order updateOrder(@PathVariable String id, @RequestBody CreateOrder order) {
        return service.updateAnOrder(id,order);
    }

    @PutMapping("{id}/complete")
    public Order completeOrder(@PathVariable String id) {
        return service.completeOrder(id);
    }


    @PostMapping("makepayment")
    public Order makePayment(@RequestBody MakePayment body) {
        return service.makePayment(body);
    }

    @GetMapping("{customerId}/customer")
    public List<Order> customerOrders(@PathVariable String customerId) {
        return service.getOrderByCustomerId(customerId);
    }

    @PutMapping("{id}/cancel")
    public Order cancelOrder(@PathVariable String id){
        return service.cancelOrder(id);
    }

    @GetMapping("{bill}/bill")
    public Order getBill(@PathVariable String bill){
        return service.getOrderByBillId(bill);
    }
}
