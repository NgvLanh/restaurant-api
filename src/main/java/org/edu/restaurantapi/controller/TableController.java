package org.edu.restaurantapi.controller;

import jakarta.validation.Valid;
import org.edu.restaurantapi.model.Table;
import org.edu.restaurantapi.request.TableRequest;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.TableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RestController
@RequestMapping("/api/tables")
public class TableController {

    @Autowired
    private TableService tableService;

    @GetMapping
    public ResponseEntity<?> getAllTables(@RequestParam(value = "branch", required = false) Optional<String> branch,
                                          @RequestParam(value = "zoneId", required = false) Long zoneId,
                                          Pageable pageable) {
        var response = tableService.getAllTables(branch, zoneId, pageable);
        return ResponseEntity.ok(ApiResponse.SUCCESS(response));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody TableRequest request) {
        var bse = tableService.findByIsDeleteFalseAndNumberAndBranchId(request.getNumber(), request.getBranchId());
        if (bse) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Số bàn đã tồn tại ở chi nhánh này"));
        }
        var response = tableService.create(request);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody TableRequest request) {
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
            @RequestParam(value = "date", required = false) String date
    ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate parsedDate = LocalDate.parse(date, formatter);
        var response = tableService.getTablesByBranchId(branch, parsedDate);
        return ResponseEntity.ok(ApiResponse.SUCCESS(response));
    }

}
