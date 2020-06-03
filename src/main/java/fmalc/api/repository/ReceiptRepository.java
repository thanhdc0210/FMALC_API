package fmalc.api.repository;

import fmalc.api.entities.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ReceiptRepository extends JpaRepository<Receipt, Integer>, JpaSpecificationExecutor<Receipt> {

}