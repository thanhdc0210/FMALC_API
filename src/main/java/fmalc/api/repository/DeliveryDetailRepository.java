package fmalc.api.repository;

import fmalc.api.entity.DeliveryDetail;
import fmalc.api.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryDetailRepository extends JpaRepository<DeliveryDetail, Integer> {

    @Query("Select d.place from DeliveryDetail d where d.consignment.id = ?1 and d.priority = ?2 and d.place.type= ?3")
    Place findDeliveryDetailByConsignmentID(int idConsignment, int priority, int typlePlace);
}
