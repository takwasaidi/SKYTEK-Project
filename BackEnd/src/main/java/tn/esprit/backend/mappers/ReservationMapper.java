package tn.esprit.backend.mappers;

import tn.esprit.backend.dto.Reservation.ReservationDTO;
import tn.esprit.backend.dto.Reservation.ReservationInfoDto;
import tn.esprit.backend.dto.Reservation.Reservation_salleDto;
import tn.esprit.backend.entity.Reservation;

import java.time.Duration;

public class ReservationMapper {
    public static ReservationDTO toDto(Reservation reservation) {
        return ReservationDTO.builder()
                .id(reservation.getId())
                .dateReservation(reservation.getDateReservation())
                .heureDebut(reservation.getHeureDebut())
                .heureFin(reservation.getHeureFin())
                .estFacturable(reservation.isEstFacturable())
                .coutTotal(reservation.getCoutTotal())
                .dateCreation(reservation.getDateCreation())
                .dateModification(reservation.getDateModification())
                .userId(reservation.getUser().getId())
                .salleId(reservation.getSalle().getId())
                .build();
    }
    public static ReservationInfoDto toInfoDto(Reservation reservation) {
        ReservationInfoDto dto = new ReservationInfoDto();
        dto.setReservationId(reservation.getId());
        dto.setSalleNom(reservation.getSalle().getNom());
        dto.setSalleTarifHoraire(reservation.getSalle().getTarifHoraire());
        dto.setDateReservation(reservation.getDateReservation());
        dto.setHeureDebut(reservation.getHeureDebut());
        dto.setHeureFin(reservation.getHeureFin());

        // Calcul de durée
        int dureeHeures = (int) Duration.between(
                reservation.getHeureDebut(),
                reservation.getHeureFin()
        ).toHours();
        dto.setDureeHeures(dureeHeures);

        dto.setEstFacturable(reservation.isEstFacturable());
        dto.setCoutTotal(reservation.getCoutTotal() != null ? reservation.getCoutTotal() : 0.0);

        return dto;
    }
    public static Reservation_salleDto toDto1(Reservation reservation) {
        return Reservation_salleDto.builder()
                .id(reservation.getId())
                .dateReservation(reservation.getDateReservation())
                .heureDebut(reservation.getHeureDebut())
                .heureFin(reservation.getHeureFin())
                .estFacturable(reservation.isEstFacturable())
                .coutTotal(reservation.getCoutTotal())
                .dateCreation(reservation.getDateCreation())
                .dateModification(reservation.getDateModification())
                .user(UserMapper.toDto(reservation.getUser()))   // Appel méthode, pas référence
                .salle(SalleMapper.toDto(reservation.getSalle())) // Appel méthode, pas référence
                .build();
    }

}
