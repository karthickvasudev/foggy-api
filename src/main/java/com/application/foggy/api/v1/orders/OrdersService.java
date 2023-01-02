package com.application.foggy.api.v1.orders;

import com.application.foggy.api.v1.customers.Customer;
import com.application.foggy.api.v1.customers.CustomersService;
import com.application.foggy.api.v1.orders.enums.ORDERSTATUS;
import com.application.foggy.api.v1.orders.enums.PAYMENTSTATUS;
import com.application.foggy.api.v1.orders.modals.CreateOrder;
import com.application.foggy.api.v1.orders.modals.MakePayment;
import com.application.foggy.api.v1.products.Product;
import com.application.foggy.api.v1.products.ProductsService;
import com.application.foggy.api.v1.transactions.PaymentTransaction;
import com.application.foggy.api.v1.transactions.PaymentTransactionService;
import com.application.foggy.documentnumbering.DocumentNumberingService;
import com.application.foggy.support.exceptionresponse.ErrorResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@AllArgsConstructor
public class OrdersService {
    private final OrdersRepository repository;
    private final DocumentNumberingService documentNumberingService;
    private final ProductsService productsService;
    private final CustomersService customersService;
    private final PaymentTransactionService paymentTransactionService;

    public Order createAnOrder(CreateOrder order) {
        Customer customer = customersService.createCustomer(order.getCustomer());
        Double totalAmount = order.getOrderLines().stream().mapToDouble(OrderLines::getPrice).sum();
        Integer totalCount = order.getOrderLines().stream().mapToInt(OrderLines::getCount).sum();
        order.setOrderLines(order.getOrderLines().stream()
                .map(o -> {
                    Product product = productsService.getProduct(o.getProductId());
                    o.setPrice(o.getCount() * product.getPrice());
                    return o;
                }).toList());
        OrderPaymentDetails orderPaymentDetails = OrderPaymentDetails.builder()
                .discount(0.0)
                .balance(order.getOrderLines().stream().mapToDouble(OrderLines::getPrice).sum())
                .paidAmount(0.0)
                .advance(0.0)
                .status(PAYMENTSTATUS.NOT_PAID)
                .build();
        Order newOrder = Order.builder()
                .id(documentNumberingService.getDocumentNumber(DocumentNumberingService.ORDER))
                .customerId(customer.getId())
                .status(ORDERSTATUS.IN_UNIT)
                .count(totalCount)
                .amount(totalAmount)
                .bill(getBillUrl())
                .orderDate(LocalDateTime.now())
                .expectedDeliveryDate(LocalDateTime.now().plus(1, ChronoUnit.DAYS))
                .orderLines(order.getOrderLines())
                .orderPaymentDetails(orderPaymentDetails)
                .paymentTransaction(new ArrayList<>())
                .createdBy("username")
                .createdOn(LocalDateTime.now())
                .build();
        documentNumberingService.increment(DocumentNumberingService.ORDER);
        Order savedOrder = repository.save(newOrder);
        return getOrder(savedOrder.getId());
    }

    public Order getOrder(String id) {
        Optional<Order> orderOpt = repository.findById(id);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setCustomer(customersService.getCustomer(order.getCustomerId()));
            List<OrderLines> orderLines = order.getOrderLines().stream()
                    .map(ol -> {
                        ol.setProduct(productsService.getProduct(ol.getProductId()));
                        return ol;
                    }).toList();
            order.setOrderLines(orderLines);
            return order;
        }
        throw new ErrorResponse(HttpStatus.NOT_FOUND, String.format("%s - order not found", id));
    }

    public List<Order> getAllOrders() {
        List<Order> allOrders = repository.findAll();
        return allOrders.stream().map(o -> getOrder(o.getId()))
                .sorted((o1, o2) -> o2.getOrderDate().compareTo(o1.getOrderDate()))
                .toList();
    }

    public Order completeOrder(String id) {
        Optional<Order> order = repository.findById(id);
        if (order.isPresent()) {
            Order order_update = order.get();
            order_update.setStatus(ORDERSTATUS.COMPLETED);
            order_update.setCompletedDate(LocalDateTime.now());
            order_update.setUpdatedBy("username");
            order_update.setUpdatedOn(LocalDateTime.now());
            Order updatedOrder = repository.save(order_update);
            return getOrder(id);
        } else
            throw new ErrorResponse(HttpStatus.NOT_FOUND, String.format("%s order not found", id));
    }

    public Order makePayment(MakePayment body) {
        Order order = getOrder(body.getId());
        OrderPaymentDetails orderPaymentDetails = order.getOrderPaymentDetails();
        Double amount = body.getAmount() + orderPaymentDetails.getPaidAmount();
        Double discount = body.getDiscount() + orderPaymentDetails.getDiscount();
        orderPaymentDetails.setPaidAmount(amount);
        orderPaymentDetails.setDiscount(discount);
        orderPaymentDetails.setBalance(order.getAmount() - amount - discount);
        if (orderPaymentDetails.getBalance() <= 0) {
            orderPaymentDetails.setBalance(0.0);
            double advance = orderPaymentDetails.getPaidAmount() + orderPaymentDetails.getDiscount() - order.getAmount();
            orderPaymentDetails.setAdvance(advance);
            orderPaymentDetails.setStatus(PAYMENTSTATUS.PAID);
            if (Objects.isNull(order.getDeliveredDate())) {
                order.setDeliveredDate(LocalDateTime.now());
            }
            order.setStatus(ORDERSTATUS.DELIVERED);
        }
        if (orderPaymentDetails.getBalance() > 0) {
            orderPaymentDetails.setStatus(PAYMENTSTATUS.PARTIALLY_PAID);
            if (Objects.isNull(order.getDeliveredDate())) {
                order.setDeliveredDate(LocalDateTime.now());
            }
            order.setStatus(ORDERSTATUS.DELIVERED);
        }
        PaymentTransaction transaction = paymentTransactionService.createTransaction(PaymentTransaction.builder()
                .orderId(order.getId())
                .paymentType(body.getPaymentType())
                .paidAmount(body.getAmount()).build());
        List<PaymentTransaction> paymentTransactionList = order.getPaymentTransaction();
        paymentTransactionList.add(transaction);
        order.setPaymentTransaction(paymentTransactionList);
        repository.save(order);
        return getOrder(order.getId());
    }

    private String getBillUrl() {
        final char[] idchars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        char[] id = new char[8];
        Random r = new Random(System.nanoTime());
        for (int i = 0; i < 8; i++) {
            id[i] = idchars[r.nextInt(idchars.length)];
        }
        return new String(id);
    }

    public List<Order> getOrderByCustomerId(String id) {
        return repository.findAllByCustomerId(id);
    }

    public Order updateAnOrder(String id, CreateOrder order) {
        Order o = getOrder(id);
        Double totalAmount = order.getOrderLines().stream().mapToDouble(OrderLines::getPrice).sum();
        Integer totalCount = order.getOrderLines().stream().mapToInt(OrderLines::getCount).sum();
        o.setCount(totalCount);
        o.setAmount(totalAmount);
        o.setOrderLines(order.getOrderLines().stream()
                .map(lines -> {
                    Product product = productsService.getProduct(lines.getProductId());
                    lines.setPrice(lines.getCount() * product.getPrice());
                    return lines;
                }).toList());
        OrderPaymentDetails orderPaymentDetails = OrderPaymentDetails.builder()
                .discount(0.0)
                .balance(order.getOrderLines().stream().mapToDouble(OrderLines::getPrice).sum())
                .paidAmount(0.0)
                .advance(0.0)
                .status(PAYMENTSTATUS.NOT_PAID)
                .build();
        o.setOrderPaymentDetails(orderPaymentDetails);
        o.setUpdatedOn(LocalDateTime.now());
        o.setUpdatedBy("updated user name");
        Order order_save = repository.save(o);
        return getOrder(order_save.getId());
    }

    public Order cancelOrder(String id) {
        Order order = getOrder(id);
        if (!order.getStatus().equals(ORDERSTATUS.IN_UNIT)) {
            throw new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, "Can't be cancel the order other than In unit status");
        }
        order.setStatus(ORDERSTATUS.CANCELED);
        order.setCanceledDate(LocalDateTime.now());
        repository.save(order);
        return order;
    }

    public Order getOrderByBillId(String bill) {
        Optional<Order> orderOpt = repository.findByBill(bill);
        if (orderOpt.isPresent()) {
            return getOrder(orderOpt.get().getId());
        }
        throw new ErrorResponse(HttpStatus.NOT_FOUND, "invalid bill reference");
    }
}
