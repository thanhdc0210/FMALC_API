package fmalc.api.repository;

import fmalc.api.entity.FuelType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuelTypeRepository extends JpaRepository<FuelType, Integer> {
}
