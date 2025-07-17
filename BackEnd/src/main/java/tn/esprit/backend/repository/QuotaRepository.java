package tn.esprit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.backend.entity.Quota;

public interface QuotaRepository extends JpaRepository<Quota,Integer> {
}
