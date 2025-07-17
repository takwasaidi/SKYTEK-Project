package tn.esprit.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Salle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nom;
    private Integer capacite;
    private Double tarifHoraire;
    private String emplacement;
    @ElementCollection
    @CollectionTable(name = "salle_images", joinColumns = @JoinColumn(name = "salle_id"))
    @Column(name = "image_url")
    private List<String> imagesUrls;
    private boolean estDisponible;
    private boolean enMaitenance;

    @OneToMany(mappedBy = "salle", cascade = CascadeType.ALL)
    private List<Reservation> reservation;
    @ManyToMany
    @JoinTable(
            name = "salle_equipment",
            joinColumns = @JoinColumn(name = "salle_id"),
            inverseJoinColumns = @JoinColumn(name = "equipment_id")
    )
    private List<Equipment> equipments;

}
