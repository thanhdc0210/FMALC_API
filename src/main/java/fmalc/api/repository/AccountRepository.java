package fmalc.api.repository;

import fmalc.api.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
<<<<<<< HEAD

import org.springframework.transaction.annotation.Transactional;
=======
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
>>>>>>> 6e6e45694c00c7664b6af08e92fc7c972bc0c0a9

@Repository
@Transactional
public interface AccountRepository extends JpaRepository<Account, Integer>, JpaSpecificationExecutor<Account> {

    @Query("select a.role from Account a where a.username = ?1 and a.password = ?2")
    String findRoleByUsernameAndPassword(String username, String password);

    List<Account> findAllBy();

    Account findByUsername(String username);
}