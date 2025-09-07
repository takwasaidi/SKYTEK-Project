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
public class Entreprise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nom;
    private String email;
    @OneToMany(mappedBy = "entreprise", cascade = CascadeType.ALL)
    private List<User> users;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "quota_id")
    private Quota quota;
    @OneToMany(mappedBy = "entreprise")
    private List<QuotaAlert> alerts;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entreprise)) return false;
        Entreprise e = (Entreprise) o;
        return id != null && id.equals(e.getId());
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
