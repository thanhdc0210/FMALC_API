package fmalc.api.repository;

import fmalc.api.entity.Consignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ConsignmentRepository
        extends JpaRepository<Consignment, Integer>, JpaSpecificationExecutor<Consignment> {

    List<Consignment> findByStatus(Integer status);
}
