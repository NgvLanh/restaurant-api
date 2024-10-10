package org.edu.restaurantapi.controller;

import jakarta.validation.Valid;
import org.edu.restaurantapi.model.Table;
import org.edu.restaurantapi.model.User;
import org.edu.restaurantapi.model.Zone;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tables")
public class TableController {

    @Autowired
    private TableService tableService;

    // Lấy thông tin một bàn theo id
    @GetMapping("/{id}")
    private ResponseEntity<?> getTable(@PathVariable Long id) {
        try {
            Table response = tableService.getTable(id);
            return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.NOT_FOUND("Not found the table with id: " + id));
        }
    }

    // Tạo mới một bàn
    @PostMapping
    private ResponseEntity<?> createTable(@Valid @RequestBody Table table) {
        try {
            // Check if a table with the same number already exists
            if (tableService.numberExists(table)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.BAD_REQUEST("Number already exists"));
            }

            Table response = tableService.createTable(table);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.CREATED(response));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.SERVER_ERROR("Created table failed: " + e.getMessage()));
        }
    }


    // Cập nhật thông tin một bàn
    @PatchMapping("/{id}")
    private ResponseEntity<?> updateTable(@PathVariable Long id, @Valid @RequestBody Table table) {
        try {
            // Check if the number exists in another table
            if (tableService.numberExists(table)) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.SERVER_ERROR("Number already exists."));
            }
            Table response = tableService.updateTable(id, table);
            return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.SERVER_ERROR("Updated table failed: " + e.getMessage()));
        }
    }


    // Xóa một bàn (đánh dấu đã xóa)
    @DeleteMapping("/{id}")
    private ResponseEntity<?> deleteTable(@PathVariable Long id) {
        try {
            Boolean response = tableService.deleteTable(id);
            if (response) {
                return ResponseEntity.ok()
                        .body(ApiResponse.DELETE("Table deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.NOT_FOUND("Table with id " + id + " not fxound"));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.SERVER_ERROR("Error deleting table status: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Table>>> getTables(
            @RequestParam(value = "number", required = false) Integer number,
            @RequestParam(value = "branchId", required = false) Long branchId,
            Pageable pageable) {
        Page<Table> response;
        if(branchId != null) {
           response  = tableService.searchTables(number, branchId, pageable);
        }else{
            response  = tableService.getTables(pageable);
        }
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }



}
