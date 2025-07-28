package tn.esprit.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.backend.dto.Reservation.EventDto;
import tn.esprit.backend.dto.Reservation.ReservationDTO;
import tn.esprit.backend.entity.Reservation;
import tn.esprit.backend.repository.ReservationRepository;
import tn.esprit.backend.service.ReservationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
public class ReservationController {
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ReservationRepository reservationRepository;

    @PostMapping
    public ResponseEntity<ReservationDTO> create(@RequestBody ReservationDTO dto) {
        return ResponseEntity.ok(reservationService.addReservation(dto));
    }

   /* @GetMapping
    public ResponseEntity<List<ReservationDTO>> getAll() {
        return ResponseEntity.ok(reservationService.getAll());
    }*/

    @GetMapping("/events")
    public List<EventDto> getReservationEvents() {
        List<Reservation> reservations = reservationRepository.findAll();

        return reservations.stream().map(res -> {
            String start = res.getDateReservation().atTime(res.getHeureDebut()).toString();
            String end = res.getDateReservation().atTime(res.getHeureFin()).toString();
            return new EventDto(
                    res.getId(),
                    "Réservation de " + (res.getUser() != null ? res.getUser().getFirstname() +res.getUser().getLastname()  : "Inconnu"),
                    start,
                    end,
                    res.getSalle() != null ? res.getSalle().getNom() : "",
                    res.getUser() != null ? res.getUser().getFirstname() : ""
            );
        }).toList();
    }


}
