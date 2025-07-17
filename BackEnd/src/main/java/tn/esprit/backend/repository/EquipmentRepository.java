package tn.esprit.backend.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.backend.entity.Equipment;

public interface EquipmentRepository extends JpaRepository<Equipment,Integer> {
}
