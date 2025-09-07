package tn.esprit.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class QuotaAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Entreprise entreprise;

    private String type; // ex: "DEPASSEMENT_QUOTA", "PROCHE_DEPASSEMENT"

    private LocalDateTime dateAlert;

    private boolean lu = false;

}
