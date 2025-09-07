package tn.esprit.backend.dto.report;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class SalleQuotaReportDto {
    private String salleNom;
    private double heuresReservees;
    private double tarifHoraire;
    private double cout;
    // New fields
    private int nombreReservations;
    private Set<String> usersSet = new HashSet<>(); // Users uniques

    // Nouveau champ pour dépassement par salle
    private double heuresDepasseesSalle;

    public SalleQuotaReportDto() {
        this.usersSet = new HashSet<>();
        this.nombreReservations = 0;
    }

    // Pour Excel : convertir Set en List
    public List<String> getUsers() {
        return new ArrayList<>(usersSet);
    }


}
