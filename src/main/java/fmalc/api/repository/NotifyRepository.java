package fmalc.api.repository;

import fmalc.api.entities.Notify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface NotifyRepository extends JpaRepository<Notify, Integer>, JpaSpecificationExecutor<Notify> {

}