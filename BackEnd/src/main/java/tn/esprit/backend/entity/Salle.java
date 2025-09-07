package tn.esprit.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

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
    private String titre;
    private String description;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
   @JoinTable(name = "salle_images" ,
   joinColumns = {
           @JoinColumn(name = "salle_id")
   },
           inverseJoinColumns = {
           @JoinColumn(name = "image_id")
           }
   )
    private Set<ImageModel> salleImages;

    private boolean estDisponible;
    private boolean enMaitenance;

    @OneToMany(mappedBy = "salle", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Reservation> reservation;
    @ManyToMany
    @JoinTable(
            name = "salle_equipment",
            joinColumns = @JoinColumn(name = "salle_id"),
            inverseJoinColumns = @JoinColumn(name = "equipment_id")
    )
    @JsonIgnore
    private List<Equipment> equipments;

}
