package tn.esprit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.backend.entity.Quota;

import java.util.Optional;

public interface QuotaRepository extends JpaRepository<Quota,Integer> {
    Optional<Quota> findByQuota(Integer quota);
}
