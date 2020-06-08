package fmalc.api.repository;

import fmalc.api.entities.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

<<<<<<< HEAD
 import org.springframework.transaction.annotation.Transactional;
=======
import org.springframework.transaction.annotation.Transactional;
>>>>>>> 6e6e45694c00c7664b6af08e92fc7c972bc0c0a9

@Repository
@Transactional
public interface AlertRepository extends JpaRepository<Alert, Integer>, JpaSpecificationExecutor<Alert> {

}