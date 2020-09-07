package fmalc.api.service;

import fmalc.api.dto.FleetManagerRequestDTO;
import fmalc.api.entity.FleetManager;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FleetManagerService {
    List<FleetManager> getAllFleet(String search);

    FleetManager save(FleetManagerRequestDTO fleetManagerRequestDTO, MultipartFile file) throws IOException;

    FleetManager findById(Integer id);


    FleetManager findByAccount(int id);

    FleetManager update(Integer id, FleetManagerRequestDTO fleetManagerRequestDTO);

    FleetManager updateAvatar(Integer id, MultipartFile file) throws IOException;

    boolean checkIdentityNo(String identityNo);

    void updateIsActive(Integer accountId, Boolean isActive, Integer fleetManagerId);
}
