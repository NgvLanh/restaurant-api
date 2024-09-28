package org.edu.restaurantapi.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.edu.restaurantapi.model.TableStatus;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.TableStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/table-statuses")
public class TableStatusController {

    @Autowired
    private TableStatusService tableStatusService;

    // Lấy thông tin một trạng thái bàn theo id
    @GetMapping("/{id}")
    private ResponseEntity<?> getTableStatus(@PathVariable Long id) {
        try {
            TableStatus response = tableStatusService.getTableStatus(id);
            return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.NOT_FOUND("Not found the table status with id: " + id));
        }
    }

    // Tạo mới một trạng thái bàn
    @PostMapping
    private ResponseEntity<?> createTableStatus(@Valid @RequestBody TableStatus tableStatus) {
        try {
            TableStatus response = tableStatusService.createTableStatus(tableStatus);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.CREATED(response));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.SERVER_ERROR("Created table status failed: " + e.getMessage()));
        }
    }

    // Cập nhật thông tin trạng thái bàn
    @PatchMapping("/{id}")
    private ResponseEntity<?> updateTableStatus(@PathVariable Long id, @RequestBody TableStatus tableStatus) {
        try {
            TableStatus response = tableStatusService.updateTableStatus(id, tableStatus);
            return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.SERVER_ERROR("Updated table status failed: " + e.getMessage()));
        }
    }

    // Xóa một trạng thái bàn (đánh dấu đã xóa)
    @DeleteMapping("/{id}")
    private ResponseEntity<?> deleteTableStatus(@PathVariable Long id) {
        try {
            Boolean response = tableStatusService.deleteTableStatus(id);
            if (response) {
                return ResponseEntity.ok()
                        .body(ApiResponse.DELETE("Table status deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.NOT_FOUND("Table status with id " + id + " not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.SERVER_ERROR("Error deleting table status: " + e.getMessage()));
        }
    }

    // Lấy danh sách các trạng thái bàn với phân trang
    @GetMapping
    private ResponseEntity<?> getTableStatuses(Pageable pageable) {
        Page<TableStatus> response = tableStatusService.getTableStatuses(pageable);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }
}
