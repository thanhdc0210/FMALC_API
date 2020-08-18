package fmalc.api.controller;

import fmalc.api.entity.Inspection;
import fmalc.api.service.InspectionService;
import fmalc.api.service.UploaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/inspection")
public class InspectionController {
    @Autowired
    InspectionService inspectionService;
    @Autowired
    UploaderService uploaderService;

    @GetMapping
    public ResponseEntity<List<Inspection>> getAllInspection() {
        List<Inspection> result = inspectionService.findAll();
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(result);
    }

    @GetMapping(value = "id/{id}")
    public ResponseEntity<Inspection> findById(@PathVariable("id") Integer id) {
        Inspection result = inspectionService.findById(id);
        if (result == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body(result);
    }

    @PostMapping
    public ResponseEntity createInspection(@RequestBody Inspection inspection) {
        try {
            inspection = inspectionService.save(inspection);
            return ResponseEntity.ok().body(inspection);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping
    public ResponseEntity<Inspection> updateInspection(@RequestBody Inspection inspection) {
        try {
            inspection = inspectionService.update(inspection);
            return ResponseEntity.ok().body(inspection);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(value = "upload-image")
    @PreAuthorize("hasRole('ROLE_DRIVER')")
    public ResponseEntity uploadImage( @RequestPart(value = "file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            String imageLink = uploaderService.upload(file);
            return ResponseEntity.ok().body(imageLink);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }
}
