package fmalc.api.repository;

import fmalc.api.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface AccountRepository extends JpaRepository<Account, Integer>, JpaSpecificationExecutor<Account> {

    @Query("select a.role from Account a where a.username = ?1 and a.password = ?2")
    String findRoleByUsernameAndPassword(String username, String password);

    Account findByUsername(String username);

    @Modifying
    @Transactional
    @Query(value = "Update account a set a.is_active =:isActive where a.id =:id", nativeQuery = true)
    void updateIsActiveById(Integer id, Boolean isActive);

    @Query("Select d.account From Driver d Where d.id = :id")
    Account findByDriverId(@Param("id") Integer id);
}