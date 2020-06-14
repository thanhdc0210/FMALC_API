package fmalc.api.repository;

import fmalc.api.entities.Consignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ConsignmentRepository
        extends JpaRepository<Consignment, Integer>, JpaSpecificationExecutor<Consignment> {

    // @Query("Select c from Consignment c Where c.status = ?1")
    List<Consignment> findByStatus(Integer status);

    @Query("Select c from Consignment c Where c.id = ?1")
    List<Consignment> findByConsignmentId(Integer id);
}
