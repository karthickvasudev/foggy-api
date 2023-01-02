package com.application.foggy.api.v1.reports;

import com.application.foggy.api.v1.orders.Order;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ThisMonthDailyReportModal {
    private String date;
    private String newOrderCount;
    private String completedOrderCount;
    private String deliveredOrderCount;
    private Double revenue;
    private Double outStanding;
    private List<Order> orders;
}
