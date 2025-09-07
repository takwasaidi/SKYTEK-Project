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
public class Quota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer quota;
    private Integer quotaUtilise;
    @OneToOne(mappedBy = "quota")
    private Entreprise entreprise;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Quota)) return false;
        Quota q = (Quota) o;
        return id != null && id.equals(q.getId());
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

}
