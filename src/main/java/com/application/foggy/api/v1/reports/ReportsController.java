package com.application.foggy.api.v1.reports;

import com.application.foggy.support.SupportedFunctions;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/reports")
@AllArgsConstructor
public class ReportsController {
    private ReportsService service;
    private SupportedFunctions functions;
    @GetMapping("allreports")
    public List<ReportModal> getAllReport(){
        return service.getAllReports();
    }

    @GetMapping("thismonthdailyreport")
    public List<ThisMonthDailyReportModal> getThisMonthReport(){
       return service.getThisMonthDailyReport();
    }
}
