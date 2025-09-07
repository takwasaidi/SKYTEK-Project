package tn.esprit.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tn.esprit.backend.dto.Reservation.EventDto;
import tn.esprit.backend.dto.Reservation.ReservationDTO;
import tn.esprit.backend.dto.Reservation.Reservation_salleDto;
import tn.esprit.backend.entity.Reservation;
import tn.esprit.backend.entity.Role;
import tn.esprit.backend.entity.User;
import tn.esprit.backend.mappers.ReservationMapper;
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
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(reservationService.addReservation(dto,loggedInUser.getId()));
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

    @GetMapping("/all/{salleId}")
    public ResponseEntity<List<EventDto>> getReservationsForUser(@PathVariable Integer salleId) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Reservation> reservations;
        List<EventDto> events;

        if (user.getRole() == Role.INTERN_USER) {
            reservations = reservationService.getReservationsByCompanyETSalle(user.getEntreprise().getId(),salleId);
        } else {
            reservations = reservationService.getReservationsByUserETSalle(user.getId(),salleId);
        }

        // Transformation en EventDto
        events = reservations.stream().map(res -> {
            String start = res.getDateReservation().atTime(res.getHeureDebut()).toString();
            String end = res.getDateReservation().atTime(res.getHeureFin()).toString();
            return new EventDto(
                    res.getId(),
                    "Réservation de " + (res.getUser() != null ? res.getUser().getFirstname() + " " + res.getUser().getLastname() : "Inconnu"),
                    start,
                    end,
                    res.getSalle() != null ? res.getSalle().getNom() : "",
                    res.getUser() != null ? res.getUser().getFirstname() : ""
            );
        }).toList();

        return ResponseEntity.ok(events);
    }
    @GetMapping("/reservations")
    public List<Reservation_salleDto> getReservations(
            @RequestParam(required = false) Boolean allByEntreprise) {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       return reservationService.getReservations(allByEntreprise,loggedInUser.getId());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> annulerReservation(@PathVariable Integer id) {
        try {
            User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            reservationService.annulerReservation(id,loggedInUser.getId());
            return ResponseEntity.ok(Map.of("message", "Réservation annulée avec succès ✅"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }


}
