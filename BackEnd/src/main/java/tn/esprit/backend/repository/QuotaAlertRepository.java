package tn.esprit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.backend.entity.QuotaAlert;

import java.util.List;

public interface QuotaAlertRepository extends JpaRepository<QuotaAlert, Long> {
    List<QuotaAlert> findByLuFalse();
    List<QuotaAlert> findByEntrepriseIdAndLuFalse(Integer entrepriseId);
    @Modifying
    @Query("UPDATE QuotaAlert n SET n.lu = true WHERE n.lu = false")
    void updateAllAsRead();
}
