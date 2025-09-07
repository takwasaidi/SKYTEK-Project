package tn.esprit.backend.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ReportingEntrepriseDto {
    private String entrepriseNom;
    private List<SalleQuotaReportDto> salleReports;
    private double totalHeuresReservees;
    private int quotaAlloue;
    private int quotaUtilise;
    private double heuresDepassees;
    private double montantFacturation;


    public ReportingEntrepriseDto() {}
}
