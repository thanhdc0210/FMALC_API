package fmalc.api.repository;

import fmalc.api.entity.FleetManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@Repository
public interface FleetManagerRepository extends JpaRepository<FleetManager, Integer> {
    @Modifying
    @Transactional
    @Query(value = "Update fleet_manager f set f.name = :name, f.identity_no = :identityNo, f.date_of_birth = :dateOfBirth  where f.id =:id", nativeQuery = true)
    void updateFleetManager(Integer id, String name, String identityNo, Date dateOfBirth);

    @Modifying
    @Transactional
    @Query(value = "Update fleet_manager f set f.image =:image where f.id =:id", nativeQuery = true)
    int updateImageById(@Param("id") Integer id, @Param("image") String image);

    @Query("select f from FleetManager f where f.account.id=?1")
    FleetManager findByAccountID(int id);

    FleetManager findByAccount_Username(String username);

    FleetManager findByAccount_UsernameAndAccount_IsActive(String username,Boolean isActive);
    boolean existsByIdentityNo(String identityNo);

    List<FleetManager> findByPhoneNumberContainingIgnoreCaseOrderByIdDesc(String search);


}
