package com.application.foggy.api.v1.reports;

import com.application.foggy.api.v1.orders.Order;
import com.application.foggy.api.v1.orders.OrdersRepository;
import com.application.foggy.api.v1.orders.enums.ORDERSTATUS;
import com.application.foggy.api.v1.orders.enums.PAYMENTSTATUS;
import com.application.foggy.support.SupportedFunctions;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReportsService {
    private SupportedFunctions functions;
    private OrdersRepository ordersRepository;

    public List<ReportModal> getAllReports() {
        List<Order> allOrders = ordersRepository.findAll();
        Set<String> monthYearSet = allOrders.stream().map(
                order -> order.getOrderDate().format(functions.getYearAndMonthFormatter()
                )).collect(Collectors.toSet());

        return monthYearSet.stream().map(monthYear -> {
            List<Order> monthYearOrderList = allOrders.stream().filter(order ->
                    order.getOrderDate().format(functions.getYearAndMonthFormatter())
                            .equals(monthYear) || Objects.nonNull(order.getCompletedDate()) && order.getCompletedDate().format(functions.getYearAndMonthFormatter())
                            .equals(monthYear) || Objects.nonNull(order.getDeliveredDate()) && order.getDeliveredDate().format(functions.getYearAndMonthFormatter())
                            .equals(monthYear)
            ).collect(Collectors.toList());

            List<String> dateList = getDateListMonthYear(monthYear);

            List<ThisMonthDailyReportModal> thisMonthDailyReportModalList = dateList.stream().map(date -> {

                List<Order> orderByDate = monthYearOrderList.stream().filter(order ->
                        Objects.nonNull(order.getOrderDate()) && order.getOrderDate().format(functions.getDateFormatter()).equals(date) ||
                                Objects.nonNull(order.getDeliveredDate()) && order.getDeliveredDate().format(functions.getDateFormatter()).equals(date) ||
                                Objects.nonNull(order.getCompletedDate()) && order.getCompletedDate().format(functions.getDateFormatter()).equals(date)
                ).collect(Collectors.toList());

                return ThisMonthDailyReportModal
                        .builder()
                        .date(date)
                        .newOrderCount(String.valueOf(orderByDate.stream().filter(o -> o.getOrderDate()
                                        .format(functions.getDateFormatter()).equals(date))
                                .collect(Collectors.toList()).size()))
                        .completedOrderCount(String.valueOf(orderByDate.stream()
                                .filter(order -> Objects.nonNull(order.getCompletedDate()) && order.getCompletedDate().format(functions.getDateFormatter()).equals(date))
                                .collect(Collectors.toList()).size()))
                        .deliveredOrderCount(String.valueOf(orderByDate.stream()
                                .filter(order -> Objects.nonNull(order.getDeliveredDate()) && order.getDeliveredDate().format(functions.getDateFormatter()).equals(date))
                                .collect(Collectors.toList()).size()))
                        .revenue(orderByDate.stream()
                                .filter(o -> Objects.nonNull(o.getDeliveredDate()) && o.getDeliveredDate().format(functions.getDateFormatter()).equals(date) &&
                                        o.getStatus().equals(ORDERSTATUS.DELIVERED) && o.getOrderPaymentDetails().getStatus().equals(PAYMENTSTATUS.PAID) || o.getOrderPaymentDetails().getStatus().equals(PAYMENTSTATUS.PARTIALLY_PAID))
                                .map(order -> order.getOrderPaymentDetails().getPaidAmount())
                                .mapToDouble(Double::doubleValue)
                                .sum())
                        .outStanding(orderByDate.stream()
                                .filter(o -> Objects.nonNull(o.getDeliveredDate()) && o.getDeliveredDate().format(functions.getDateFormatter()).equals(date) &&
                                        o.getStatus().equals(ORDERSTATUS.DELIVERED) && o.getOrderPaymentDetails().getStatus().equals(PAYMENTSTATUS.NOT_PAID) || o.getOrderPaymentDetails().getStatus().equals(PAYMENTSTATUS.PARTIALLY_PAID))
                                .map(order -> order.getOrderPaymentDetails().getBalance())
                                .mapToDouble(Double::doubleValue)
                                .sum())
                        .orders(orderByDate)
                        .build();
            }).collect(Collectors.toList());


            return ReportModal.builder()
                    .monthWithYear(monthYear)
                    .ordersCount(String.valueOf(thisMonthDailyReportModalList.stream()
                            .map(order -> {
                                try {

                                    return Objects.nonNull(order.getCompletedOrderCount()) ? Integer.valueOf(order.getCompletedOrderCount()) : 0;
                                } catch (Exception e) {
                                    return 0;
                                }
                            })
                            .mapToInt(Integer::intValue).sum()))
                    .revenue(thisMonthDailyReportModalList.stream().map(order -> order.getRevenue())
                            .mapToDouble(Double::doubleValue).sum())
                    .outStanding(thisMonthDailyReportModalList.stream().map(order -> order.getOutStanding())
                            .mapToDouble(Double::doubleValue).sum())
                    .monthReport(thisMonthDailyReportModalList)
                    .build();
        }).collect(Collectors.toList());
    }

    private List<String> getDateListMonthYear(String monthYear) {
        LocalDate startDay = YearMonth.parse(monthYear, functions.getYearAndMonthFormatter()).atDay(1);
        LocalDate tillDate;
        if (YearMonth.parse(LocalDate.now().format(functions.getYearAndMonthFormatter()), functions.getYearAndMonthFormatter()).atDay(1)
                .equals(YearMonth.parse(monthYear, functions.getYearAndMonthFormatter()).atDay(1))) {
            return getDateList();
        } else {
            tillDate = startDay.withDayOfMonth(startDay.getMonth().length(startDay.isLeapYear()));
        }
        List<String> dateList = new ArrayList<>();
        while (!startDay.isAfter(tillDate)) {
            dateList.add(startDay.format(functions.getDateFormatter()));
            startDay = startDay.plusDays(1);
        }
        return dateList;
    }

    private List<String> getDateList() {
        LocalDate startDay = LocalDate.now().withDayOfMonth(1);
        LocalDate tillDate = LocalDate.now();
        List<String> dateList = new ArrayList<>();
        while (!startDay.isAfter(tillDate)) {
            dateList.add(startDay.format(functions.getDateFormatter()));
            startDay = startDay.plusDays(1);
        }
        return dateList;
    }

    public List<ThisMonthDailyReportModal> getThisMonthDailyReport(){
        List<String> dateList = getDateList();
        List<Order> allOrders = ordersRepository.findAll();

        return dateList.stream().map(date -> {
                    List<Order> orders = allOrders.stream()
                            .filter(order -> Objects.nonNull(order.getOrderDate()) && order.getOrderDate().format(functions.getDateFormatter()).equals(date)
                                    || Objects.nonNull(order.getCompletedDate()) && order.getCompletedDate().format(functions.getDateFormatter()).equals(date)
                                    || Objects.nonNull(order.getDeliveredDate()) && order.getDeliveredDate().format(functions.getDateFormatter()).equals(date)
                            ).collect(Collectors.toList());

                    String newOrderCount = String.valueOf(orders.stream()
                            .filter(order -> Objects.nonNull(order.getOrderDate()) && order.getOrderDate().format(functions.getDateFormatter()).equals(date))
                            .collect(Collectors.toList()).size());

                    String completedOrdersCount = String.valueOf(orders.stream()
                            .filter(order -> Objects.nonNull(order.getCompletedDate()) && order.getCompletedDate().format(functions.getDateFormatter()).equals(date))
                            .collect(Collectors.toList()).size());

                    String deliveredOrdersCount = String.valueOf(orders.stream()
                            .filter(order -> Objects.nonNull(order.getDeliveredDate()) && order.getDeliveredDate().format(functions.getDateFormatter()).equals(date))
                            .collect(Collectors.toList()).size());

                    Double revenue = orders.stream()
                            .filter(order -> Objects.nonNull(order.getDeliveredDate())
                                    && order.getDeliveredDate()
                                    .format(functions.getDateFormatter())
                                    .equals(date)
                                    && order.getOrderPaymentDetails().getStatus().equals(PAYMENTSTATUS.PAID)
                                    || order.getOrderPaymentDetails().getStatus().equals(PAYMENTSTATUS.PARTIALLY_PAID))
                            .map(order -> order.getOrderPaymentDetails().getPaidAmount())
                            .mapToDouble(Double::doubleValue).sum();
                    Double outStanding = orders.stream()
                            .filter(order -> Objects.nonNull(order.getDeliveredDate())
                                    && order.getDeliveredDate()
                                    .format(functions.getDateFormatter())
                                    .equals(date))
                            .map(order -> order.getOrderPaymentDetails().getBalance())
                            .mapToDouble(Double::doubleValue).sum();
                    return ThisMonthDailyReportModal.builder()
                            .date(date)
                            .newOrderCount(newOrderCount)
                            .completedOrderCount(completedOrdersCount)
                            .deliveredOrderCount(deliveredOrdersCount)
                            .revenue(revenue)
                            .outStanding(outStanding)
                            .orders(orders).build();
                }).sorted((o1, o2) -> o2.getDate().compareTo(o1.getDate()))
                .collect(Collectors.toList());
    }
}
