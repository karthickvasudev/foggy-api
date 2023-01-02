package com.application.foggy.api.v1.reports;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReportModal {
    private String monthWithYear;
    private String ordersCount;
    private Double revenue;
    private Double outStanding;
    private List<ThisMonthDailyReportModal> monthReport;
}