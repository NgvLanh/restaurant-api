package org.edu.restaurantapi.controller;

import jakarta.validation.Valid;
import org.edu.restaurantapi.model.Table;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/tables")
public class TableController {

    @Autowired
    private TableService tableService;

    @GetMapping
    public ResponseEntity<?> getAllTables(@RequestParam(value = "branch", required = false) Optional<String> branch,
                                          Pageable pageable) {
        var response = tableService.getAllTables(branch, pageable);
        return ResponseEntity.ok(ApiResponse.SUCCESS(response));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Table request) {
        var bse = tableService.findByIsDeleteFalseAndNumberAndBranchIs(request.getNumber(), request.getBranch());
        if (bse) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Số bàn đã tồn tại ở chi nhánh này"));
        }
        var response = tableService.create(request);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Table request) {
//        var bse = service.findByNameAndIdNot(request.getName(), id);
//        if (bse) {
//            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Tên trạng thái đã tồn tại"));
//        }
        var response = tableService.update(id, request);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        var response = tableService.delete(id);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @GetMapping("/reservations")
    public ResponseEntity<?> getTablesByBranchIdAndSeats(
            @RequestParam(value = "branch", required = false) Optional<Long> branch,
            @RequestParam(value = "time", required = false) Optional<String> time
    ) {
        var response = tableService.getTablesByBranchId(branch, time);
        return ResponseEntity.ok(ApiResponse.SUCCESS(response));
    }

}
