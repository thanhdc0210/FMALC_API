package fmalc.api.controller;

import fmalc.api.dto.FLeetManagerResponseDTO;
import fmalc.api.dto.FleetManagerRequestDTO;
import fmalc.api.entity.FleetManager;
import fmalc.api.service.FleetManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/fleetmanager")
public class FleetManagerController {

    @Autowired
    FleetManagerService fleetManagerService;

    @GetMapping
    public ResponseEntity<List<FLeetManagerResponseDTO>> getAllFleetManager(@RequestParam(value = "search", defaultValue = "") String search) {
        try {
            FLeetManagerResponseDTO fLeetManagerResponseDTO = new FLeetManagerResponseDTO();
            List<FLeetManagerResponseDTO> fleetManagerResponseDTOS = new ArrayList<>();
            List<FleetManager> fleetManagers = fleetManagerService.getAllFleet(search);
            if (fleetManagers.size() > 0) {
                fleetManagerResponseDTOS = fLeetManagerResponseDTO.mapToListResponse(fleetManagers);
                return ResponseEntity.ok().body(fleetManagerResponseDTOS);
            } else {
                return ResponseEntity.noContent().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity createFleetManager(@RequestPart(value = "file") MultipartFile file, @ModelAttribute FleetManagerRequestDTO fleetManagerRequestDTO) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            FleetManager fleetManager = fleetManagerService.save(fleetManagerRequestDTO, file);
            return ResponseEntity.ok().body(new FLeetManagerResponseDTO().mapToResponse(fleetManager));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "id/{id}")
    public ResponseEntity<FLeetManagerResponseDTO> findById(@PathVariable("id") Integer id) {
        FleetManager fleetManager = fleetManagerService.findById(id);
        if (fleetManager == null) {
            return ResponseEntity.badRequest().build();
        }
        FLeetManagerResponseDTO result = new FLeetManagerResponseDTO().mapToResponse(fleetManager);
        return ResponseEntity.ok().body(result);
    }

    @PutMapping(value = "id/{id}")
    public ResponseEntity<FLeetManagerResponseDTO> updateFleetManager(@PathVariable("id") Integer id, @RequestBody FleetManagerRequestDTO fleetManagerRequestDTO) {
        try {
            FleetManager fleetManager = fleetManagerService.update(id, fleetManagerRequestDTO);
            return ResponseEntity.ok().body(new FLeetManagerResponseDTO().mapToResponse(fleetManager));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = "update-avatar/{id}")
    public ResponseEntity updateAvatar(@PathVariable("id") Integer id, @RequestPart(value = "file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            FleetManager fleetManager = fleetManagerService.updateAvatar(id, file);
            return ResponseEntity.ok().body(new FLeetManagerResponseDTO().mapToResponse(fleetManager));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "check-identity-no")
    boolean checkVehiclePlates(@RequestParam("identityNo") String identityNo) {
        return fleetManagerService.checkIdentityNo(identityNo);
    }

    @PutMapping(value = "is-active/{accountId}")
    public ResponseEntity updateIsActive(@PathVariable("accountId") Integer accountId,
                                         @RequestParam("isActive") Boolean isActive,
                                         @RequestParam(value = "fleetManagerId", required = false) Integer fleetManagerId) {
        try {
            fleetManagerService.updateIsActive(accountId, isActive, fleetManagerId);
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

}
