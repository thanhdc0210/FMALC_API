package fmalc.api.repository;

import fmalc.api.entity.FuelType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface FuelTypeRepository extends JpaRepository<FuelType, Integer> {
    FuelType findByType(String type);

    @Modifying
    @Transactional
    @Query(value = "Update fuel_type f set f.current_price =:currentPrice where f.id =:id", nativeQuery = true)
    void updatePrice(Integer id, Double currentPrice);
}
