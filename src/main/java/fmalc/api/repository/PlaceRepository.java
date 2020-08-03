package fmalc.api.repository;

import fmalc.api.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Integer> {

    @Query("select p from Place p where p.consignment.id =?1")
    List<Place> getPlaceOfConsignment(int idConsignment);

    @Query("select p from Place p where p.consignment.id =?1 and p.priority = ?2")
    List<Place> getPlaceByPriority(int idConsignment, int priority);

    @Query("select p from Place p where p.consignment.id =?1 and p.type = ?2")
    List<Place> getPlaceByTypePlace(int idConsignment, int typePlace);

    @Query("select p from Place p where p.consignment.id =?1 and p.priority = ?2 and p.type =?3")
    Place getPlaceByTypePlaceAndPriority(int idConsignment, int priority, int typePlace);

    Place findById(int id);
    Place findPlaceById(int id);
}
