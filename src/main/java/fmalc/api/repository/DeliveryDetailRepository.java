//package fmalc.api.repository;
//
//import fmalc.api.entity.DeliveryDetail;
//import fmalc.api.entity.Place;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface ADeliveryDetailRepository extends JpaRepository<DeliveryDetail, Integer> {
//
//    @Query("Select d.place from DeliveryDetail d where d.consignment.id = ?1 and d.priority = ?2 and d.place.type= ?3")
//    Place findDeliveryDetailByConsignmentID(int idConsignment, int priority, int typlePlace);
//
//    @Query("select d from DeliveryDetail  d where d.consignment.id = ?1")
//    List<DeliveryDetail> findDeliveryDetailByConsignment(int idConsignemnt);
//
//    @Query("Select d.place from DeliveryDetail d where d.consignment.id = ?1 and d.priority = ?2 and d.place.type= ?3")
//    List<Place> getListPlace(int idConsignment, int typlePlace);
//}
