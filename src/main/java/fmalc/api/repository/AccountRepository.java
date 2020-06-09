package fmalc.api.repository;

import fmalc.api.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
@Transactional
public interface AccountRepository extends JpaRepository<Account, Integer>, JpaSpecificationExecutor<Account> {

    @Query("select a.role from Account a where a.username = ?1 and a.password = ?2")
    String findRoleByUsernameAndPassword(String username, String password);

    Account findByUsername(String username);
}