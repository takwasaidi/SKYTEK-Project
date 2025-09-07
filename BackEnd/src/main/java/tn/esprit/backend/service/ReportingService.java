package tn.esprit.backend.service;

import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.backend.dto.report.ReportingEntrepriseDto;
import tn.esprit.backend.dto.report.SalleQuotaReportDto;
import tn.esprit.backend.entity.Entreprise;
import tn.esprit.backend.entity.Quota;
import tn.esprit.backend.entity.Reservation;
import tn.esprit.backend.entity.Salle;
import tn.esprit.backend.repository.EntrepriseRepository;
import tn.esprit.backend.repository.QuotaRepository;
import tn.esprit.backend.repository.ReservationRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ReportingService {
    @Autowired
    private EntrepriseRepository entrepriseRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private QuotaRepository quotaRepository;
    @Autowired
    private MailAttachmentService mailService;

    public List<ReportingEntrepriseDto> generateReporting() {
        List<Entreprise> entreprises = entrepriseRepository.findAll();

        List<ReportingEntrepriseDto> reports = new ArrayList<>();

        for (Entreprise entreprise : entreprises) {
            Quota quota = entreprise.getQuota();
            int quotaAlloue = quota != null ? quota.getQuota() : 0;
            int quotaUtilise = quota != null ? quota.getQuotaUtilise() : 0;

            // Récupérer toutes les réservations de cette entreprise
            List<Reservation> reservations = reservationRepository.findByUserEntrepriseId(entreprise.getId());

            Map<Integer, SalleQuotaReportDto> salleMap = new HashMap<>();
            double totalHeures = 0;
            double coutTotalFacturable = 0;

            for (Reservation r : reservations) {
                // Durée en heures
                double dureeHeures = Duration.between(r.getHeureDebut(), r.getHeureFin()).toMinutes() / 60.0;
                totalHeures += dureeHeures;

                Salle salle = r.getSalle();
                SalleQuotaReportDto dto = salleMap.get(salle.getId());

                if (dto == null) {
                    dto = new SalleQuotaReportDto();
                    dto.setSalleNom(salle.getNom());
                    dto.setHeuresReservees(0);
                    dto.setTarifHoraire(salle.getTarifHoraire());
                    dto.setCout(0);
                    dto.setNombreReservations(0);        // initialize
                    dto.setUsersSet(new HashSet<>());       // initialize as empty Set for unique users    // initialize
                    dto.setHeuresDepasseesSalle(0); // init
                    salleMap.put(salle.getId(), dto);
                }
                dto.setHeuresReservees(dto.getHeuresReservees() + dureeHeures);

                if (r.isEstFacturable()) {
                    coutTotalFacturable += r.getCoutTotal();
                    dto.setCout(dto.getCout() + r.getCoutTotal());

                    // ✅ Ajout direct des heures facturables (dépassement par salle)
                    dto.setHeuresDepasseesSalle(dto.getHeuresDepasseesSalle() + dureeHeures);
                }
                // Increment number of reservations
                dto.setNombreReservations(dto.getNombreReservations() + 1);

                // Add user full name only if not already in the list
                String fullName = r.getUser().getFirstname() + " " + r.getUser().getLastname();
                dto.getUsersSet().add(fullName); // ajoute automatiquement uniquement si unique
// Debug : afficher tous les utilisateurs capturés pour cette salle
                System.out.println("Salle : " + dto.getSalleNom() + " | Users : " + dto.getUsersSet());
            }

            // Calcul des heures dépassées (positive ou 0)
            double heuresDepassees = Math.max(0, quotaUtilise - quotaAlloue);

            // Calcul facturation selon quota utilisé
            double montantFacturation = 0;
            if (quotaUtilise > quotaAlloue) {
                montantFacturation = coutTotalFacturable;
            }

            ReportingEntrepriseDto report = new ReportingEntrepriseDto();
            report.setEntrepriseNom(entreprise.getNom());
            report.setSalleReports(new ArrayList<>(salleMap.values()));
            report.setTotalHeuresReservees(totalHeures);
            report.setQuotaAlloue(quotaAlloue);
            report.setQuotaUtilise(quotaUtilise);
            report.setHeuresDepassees(heuresDepassees);
            report.setMontantFacturation(montantFacturation);


            reports.add(report);
        }

        return reports;
    }

public ByteArrayInputStream generateExcelReport() throws IOException {
    List<ReportingEntrepriseDto> reports = generateReporting();

    try (Workbook workbook = new XSSFWorkbook()) {
        Sheet sheet = workbook.createSheet("Quota Report");

        // Styles
        Font fontBold = workbook.createFont();
        fontBold.setBold(true);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(fontBold);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle entrepriseStyle = workbook.createCellStyle();
        entrepriseStyle.setFont(fontBold);
        entrepriseStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        entrepriseStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle overQuotaStyle = workbook.createCellStyle();
        overQuotaStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
        overQuotaStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle totalStyle = workbook.createCellStyle();
        totalStyle.setFont(fontBold);
        totalStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        totalStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        int rowIdx = 0;

        for (ReportingEntrepriseDto report : reports) {
            // Entreprise Header
            Row entrepriseRow = sheet.createRow(rowIdx++);
            Cell entCell = entrepriseRow.createCell(0);
            entCell.setCellValue(report.getEntrepriseNom());
            entCell.setCellStyle(entrepriseStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowIdx-1, rowIdx-1, 0, 5));

            // Header for salle details
            Row headerRow = sheet.createRow(rowIdx++);
            String[] headers = {"Salle", "Heures Réservées", "Tarif Horaire (TND)", "Coût Salle (TND)", "Nombre Réservations", "Utilisateur Responsable"   };
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Salle details
            for (SalleQuotaReportDto salleReport : report.getSalleReports()) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(salleReport.getSalleNom());
                row.createCell(1).setCellValue(salleReport.getHeuresReservees());
                row.createCell(2).setCellValue(salleReport.getTarifHoraire());
                // 🔹 Display cost with a textual breakdown, not a real calculation
                String coutDisplay = String.format(
                        "%.2f = (%.0f * %.0f)",
                        salleReport.getCout(),
                        salleReport.getTarifHoraire(),
                        salleReport.getHeuresDepasseesSalle() // <- heures dépassées de CETTE salle
                );
                row.createCell(3).setCellValue(coutDisplay);

                row.createCell(4).setCellValue(salleReport.getNombreReservations());
                row.createCell(5).setCellValue(String.join(", ", salleReport.getUsers())); // List of users

            }

            // Totals and Quota info
            Row totalRow = sheet.createRow(rowIdx++);
            totalRow.createCell(0).setCellValue("TOTAL");
            totalRow.getCell(0).setCellStyle(totalStyle);

            totalRow.createCell(1).setCellValue(report.getTotalHeuresReservees());
            totalRow.createCell(2).setCellValue("Quota Alloué: " + report.getQuotaAlloue());
            totalRow.createCell(3).setCellValue("Quota Utilisé: " + report.getQuotaUtilise());
            totalRow.createCell(4).setCellValue("Heures Dépassées: " + report.getHeuresDepassees());
            totalRow.createCell(5).setCellValue("Montant Facturation: " + report.getMontantFacturation());

            // Conditional formatting for over quota
            if (report.getHeuresDepassees() > 0) {
                for (int i = 0; i <= 5; i++) {
                    totalRow.getCell(i).setCellStyle(overQuotaStyle);
                }
            }

            // Empty row for spacing
            rowIdx++;
        }

        // Auto-size columns
        for (int i = 0; i <= 5; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        return new ByteArrayInputStream(out.toByteArray());
    }
}
    // @Scheduled(cron = "*/10 * * * * ?") // Every 10 seconds
    @Scheduled(cron = "0 0 0 1 * ?") // At 00:00 on the 1st day of every month
    public void generateMonthlyExcelReport() {
        try {
            // Generate Excel report
            ByteArrayInputStream in = generateExcelReport();
            byte[] reportBytes = in.readAllBytes();

            // Send email
            mailService.sendMailWithAttachment(
                    "stakwa336@gmail.com",              // recipient
                    "Monthly Quota Report",           // subject
                    "Please find attached the quota report for this month.", // text
                    reportBytes,                      // attachment bytes
                    "quota_report.xlsx"               // attachment filename
            );

            System.out.println("Monthly report sent by email successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
