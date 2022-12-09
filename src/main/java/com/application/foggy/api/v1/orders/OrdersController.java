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
    public Order get(@PathVariable String id){
        return service.getOrder(id);
    }

    @GetMapping()
    public List<Order> list(){
        return service.getAllOrders();
    }

    @PutMapping("{id}")
    public Order completeOrder(@PathVariable String id){
        return service.completeOrder(id);
    }

    @PostMapping("makepayment")
    public Order makePayment(@RequestBody MakePayment body){
        return service.makePayment(body);
    }


}
