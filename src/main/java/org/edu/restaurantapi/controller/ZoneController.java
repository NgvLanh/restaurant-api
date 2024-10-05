package org.edu.restaurantapi.controller;

import jakarta.validation.Valid;
import org.edu.restaurantapi.model.TableStatus;
import org.edu.restaurantapi.model.Zone;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/zones")
public class ZoneController {

    @Autowired
    private ZoneService zoneService;

    // Lấy thông tin Zone
    @GetMapping("/{id}")
    private ResponseEntity<?> getZone(@PathVariable Long id) {
        try {
            Zone response = zoneService.getZone(id);
            return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.NOT_FOUND("Not found the zone with id: " + id));
        }
    }

    // Tạo Zone mới
    @PostMapping
    private ResponseEntity<?> createZone(@Valid @RequestBody Zone zone) {
        if (zoneService.zoneNameExists(zone)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.BAD_REQUEST("Zone name already exists"));
        } else {
            try {
                Zone response = zoneService.createZone(zone);
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(ApiResponse.CREATED(response));
            } catch (Exception e) {
                return ResponseEntity.internalServerError()
                        .body(ApiResponse.SERVER_ERROR("Created zone failed: " + e.getMessage()));
            }
        }
    }

    // Cập nhật Zone
    @PatchMapping("/{id}")
    private ResponseEntity<?> updateZone(@PathVariable Long id, @RequestBody Zone zone) {
        if (zoneService.zoneNameExists(zone)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.BAD_REQUEST("Zone name already exists"));
        } else {
            try {
                Zone response = zoneService.updateZone(id, zone);
                return ResponseEntity.ok()
                        .body(ApiResponse.SUCCESS(response));
            } catch (Exception e) {
                return ResponseEntity.internalServerError()
                        .body(ApiResponse.SERVER_ERROR("Updated zone failed: " + e.getMessage()));
            }
        }
    }

    // Xóa Zone
    @DeleteMapping("/{id}")
    private ResponseEntity<?> deleteZone(@PathVariable Long id) {
        try {
            Boolean response = zoneService.deleteZone(id);
            if (response) {
                return ResponseEntity.ok()
                        .body(ApiResponse.DELETE("Zone deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.NOT_FOUND("Zone with id " + id + " not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.SERVER_ERROR("Error deleting zone: " + e.getMessage()));
        }
    }

    // Lấy danh sách các Zone
    @GetMapping
    public ResponseEntity<ApiResponse<Page<Zone>>> getZones(
            @RequestParam(value = "address", required = false) String address,
            Pageable pageable) {
        Page<Zone> response;

        // Kiểm tra nếu address là null hoặc rỗng
        if (address != null && !address.isEmpty()) {
            response = zoneService.getZoneByAddress(address, pageable);
        } else {
            response = zoneService.getZones(pageable);
        }

        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

}
