package tn.esprit.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.backend.dto.report.ReportingEntrepriseDto;
import tn.esprit.backend.service.ReportingService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;



@RestController
@RequestMapping("/api/reporting")
public class ReportingController {
    @Autowired
    private ReportingService reportingService;

    @GetMapping("/quota")
    public List<ReportingEntrepriseDto> getQuotaReport() {
        return reportingService.generateReporting();
    }
    @GetMapping("/quota/excel")
    public ResponseEntity<Resource> downloadExcelReport() throws IOException {
        ByteArrayInputStream in = reportingService.generateExcelReport();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=quota_report.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(in));
    }

}
