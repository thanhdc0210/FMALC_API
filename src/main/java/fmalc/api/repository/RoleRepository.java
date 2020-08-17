package fmalc.api.repository;

import fmalc.api.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRole(String role);

//    @Query("select r from Role r where r.accounts")
//    Role findByAccounts(int idAccount);
}
