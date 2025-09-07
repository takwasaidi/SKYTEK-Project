package tn.esprit.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.backend.dto.salle.SalleDto;
import tn.esprit.backend.dto.salle.SallePopulaireDTO;
import tn.esprit.backend.entity.ConfigurationHoraire;
import tn.esprit.backend.entity.Equipment;
import tn.esprit.backend.entity.Reservation;
import tn.esprit.backend.entity.Salle;
import tn.esprit.backend.mappers.SalleMapper;
import tn.esprit.backend.repository.ConfigurationHoraireRepository;
import tn.esprit.backend.repository.EquipmentRepository;
import tn.esprit.backend.repository.ReservationRepository;
import tn.esprit.backend.repository.SalleRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalleService {
    private final SalleRepository salleRepository;
    private final EquipmentRepository equipmentRepository;
    private final ConfigurationHoraireRepository configHoraireRepository;
    private final ReservationRepository reservationRepository;

   public Salle save(Salle salle) {
        return salleRepository.save(salle);
    }

    public List<SalleDto> findAll() {
        return salleRepository.findAll()
                .stream()
                .map(SalleMapper::toDto)
                .collect(Collectors.toList());
    }

    public SalleDto findById(Integer id) {
        return salleRepository.findById(id)
                .map(SalleMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Salle not found"));
    }
    public Optional<Salle> findSalleById(Integer id) {
        return salleRepository.findById(id);
    }

    public void delete(Integer id) {
        salleRepository.deleteById(id);
    }

    public SalleDto update(Integer id, SalleDto dto) {
        Salle existing = salleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Salle not found"));

        existing.setNom(dto.getNom());
        existing.setCapacite(dto.getCapacite());
        existing.setTarifHoraire(dto.getTarifHoraire());
        existing.setEmplacement(dto.getEmplacement());
        existing.setEstDisponible(dto.isEstDisponible());
        existing.setEnMaitenance(dto.isEnMaitenance());

        if (dto.getEquipmentIds() != null) {
            List<Equipment> equipments = equipmentRepository.findAllById(dto.getEquipmentIds());
            existing.setEquipments(equipments);
        }

        return SalleMapper.toDto(salleRepository.save(existing));
    }

    public List<LocalTime> getCreneauxDisponibles(Integer salleId, LocalDate date) {
        // 1. Trouver le jour de la semaine
        DayOfWeek jour = date.getDayOfWeek();

        // 2. Charger la configuration horaire pour ce jour
        ConfigurationHoraire config = configHoraireRepository.findByJour(jour)
                .orElseThrow(() -> new RuntimeException("Pas de config pour ce jour"));

        if (!config.isEstOuvert()) {
            return Collections.emptyList();
        }

        // 3. Lister les créneaux disponibles par tranche (par exemple toutes les heures)
        List<LocalTime> tousCreneaux = new ArrayList<>();
        LocalTime current = config.getHeureDebut();
        while (current.plusMinutes(60).isBefore(config.getHeureFin().plusSeconds(1))) {
            tousCreneaux.add(current);
            current = current.plusHours(1); // changer à .plusMinutes(30) si besoin
        }

        // 4. Exclure les heures déjà prises
        List<Reservation> reservations = reservationRepository.findBySalleIdAndDateReservation(salleId, date);

        for (Reservation r : reservations) {
            tousCreneaux.removeIf(c ->
                    !c.isBefore(r.getHeureDebut()) && c.isBefore(r.getHeureFin())
            );
        }

        return tousCreneaux;
    }


    public List<SalleDto> findAvailableSalles(LocalDate date, LocalTime heureDebut, LocalTime heureFin) {
        // Ids des salles occupées
        List<Integer> occupiedIds = salleRepository.findOccupiedSalleIds(date, heureDebut, heureFin);

        // Toutes les salles sauf celles occupées
        List<Salle> availableSalles = salleRepository.findAll()
                .stream()
                .filter(s -> !occupiedIds.contains(s.getId()))
                .toList();

        return availableSalles.stream()
                .map(SalleMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<SallePopulaireDTO> getSallesPopulaires(Integer entrepriseId) {
        // Récupérer toutes les réservations dont l'utilisateur appartient à l'entreprise
        List<Reservation> reservations = reservationRepository.findAll().stream()
                .filter(r -> r.getUser() != null &&
                        r.getUser().getEntreprise() != null &&
                        r.getUser().getEntreprise().getId().equals(entrepriseId))
                .collect(Collectors.toList());

        // Compter le nombre de réservations par salle
        Map<Integer, Long> reservationsParSalle = reservations.stream()
                .collect(Collectors.groupingBy(r -> r.getSalle().getId(), Collectors.counting()));

        // Construire les DTO avec le nom de la salle
        List<SallePopulaireDTO> result = new ArrayList<>();
        for (Map.Entry<Integer, Long> entry : reservationsParSalle.entrySet()) {
            Salle salle = salleRepository.findById(entry.getKey()).orElse(null);
            if (salle != null) {
                result.add(new SallePopulaireDTO(salle.getNom(), entry.getValue().intValue()));
            }
        }

        // Trier décroissant et limiter au top 5
        return result.stream()
                .sorted((a, b) -> b.getReservations() - a.getReservations())
                .limit(5)
                .collect(Collectors.toList());
    }


}
