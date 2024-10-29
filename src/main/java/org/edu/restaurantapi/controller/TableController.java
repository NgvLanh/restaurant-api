package org.edu.restaurantapi.controller;

import jakarta.validation.Valid;
import org.edu.restaurantapi.model.Table;
import org.edu.restaurantapi.model.Table;
import org.edu.restaurantapi.model.User;
import org.edu.restaurantapi.model.Zone;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.TableService;
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
    private TableService service;

    @GetMapping
    public ResponseEntity<?> gets(@RequestParam(value = "branch", required = false) String branch,
                                  @RequestParam(value = "number", required = false) String number,
                                  Pageable pageable) {
        var response = service.gets(branch, number, pageable);
        return ResponseEntity.ok(ApiResponse.SUCCESS(response));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Table request) {
        var bse = service.findByIsDeleteFalseAndNumberAndBranchIs(request.getNumber(), request.getBranch());
        if (bse) {
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Số bàn đã tồn tại ở chi nhánh này"));
        }
        var response = service.create(request);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Table request) {
//        var bse = service.findByNameAndIdNot(request.getName(), id);
//        if (bse) {
//            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Tên trạng thái đã tồn tại"));
//        }
        var response = service.update(id, request);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        var response = service.delete(id);
        return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
    }

}
