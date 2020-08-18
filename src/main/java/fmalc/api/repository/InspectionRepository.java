package fmalc.api.repository;

import fmalc.api.entity.Inspection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface InspectionRepository extends JpaRepository<Inspection, Integer> {
    @Modifying
    @Transactional
    @Query(value = "Update inspection i set i.inspection_name =:inspectionName where i.id =:id", nativeQuery = true)
    void update(Integer id, String inspectionName);

    List<Inspection> findAllByOrderByIdAsc();

    List<Inspection> findAllByOrderByIdDesc();
}
