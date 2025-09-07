package tn.esprit.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ReclamationFichier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nomFichier;
    private String typeMime;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] contenu;


    @ManyToOne
    @JoinColumn(name = "reclamation_id")
    private Reclamation reclamation;
}
