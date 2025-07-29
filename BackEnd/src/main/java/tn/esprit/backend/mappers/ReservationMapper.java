package tn.esprit.backend.mappers;

import tn.esprit.backend.dto.Reservation.ReservationDTO;
import tn.esprit.backend.entity.Reservation;

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
}
