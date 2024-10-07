package org.edu.restaurantapi.controller;

import jakarta.validation.Valid;
import org.edu.restaurantapi.model.Discount;
import org.edu.restaurantapi.model.DiscountMethod;
import org.edu.restaurantapi.model.User;
import org.edu.restaurantapi.response.ApiResponse;
import org.edu.restaurantapi.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/discounts")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @GetMapping
    public ResponseEntity<?> getDiscount(@RequestParam(value = "code", required = false) String code,Pageable pageable) {
        Page<Discount> response;
        if(code!= null && !code.isEmpty()){
            response = discountService.getDiscountMethodByValue(code,pageable);
        }else{
            response = discountService.getAllDiscounts(pageable);
        }
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.SUCCESS(response));
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getDiscountById(@PathVariable Long id) {
        try {
            Discount response = discountService.getDiscount(id);
            if (response==null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.NOT_FOUND("Not found the discount  with id: " + id));
            }
            return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.NOT_FOUND("Not found the discount with id: " + id));
        }
    }

    // Tạo discount mới
    @PostMapping
    public ResponseEntity<?> createDiscount(@Valid @RequestBody Discount discount) {
        if (discountService.discountCodeExists(discount)){
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Code already exists"));
        }else {
            try {
                Discount response = discountService.createDiscount(discount);

                return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.CREATED(response));
            }catch (Exception e){
                return  ResponseEntity.internalServerError()
                        .body(ApiResponse.SERVER_ERROR("Created discount failed" + e.getMessage()));

            }
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateDiscount(@PathVariable Long id, @RequestBody Discount discount) {
        if (discountService.discountCodeExists(discount) &&
                discountService.getDiscount(discount.getId()).getId() != discount.getId()){
            return ResponseEntity.badRequest().body(ApiResponse.BAD_REQUEST("Code already exists"));
        }else {
            try {
                Discount response = discountService.updateDiscount(id,discount);
                if (response==null){
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(ApiResponse.NOT_FOUND("Not found the discount method with id: " + id));
                }
                return ResponseEntity.ok().body(ApiResponse.SUCCESS(response));
            }catch (Exception e){
                return  ResponseEntity.internalServerError()
                        .body(ApiResponse.SERVER_ERROR("Update discount failed" + e.getMessage()));

            }
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDiscount(@PathVariable Long id) {
        try {
            Boolean response = discountService.deleteDiscount(id);
            if (response){
                return ResponseEntity.ok().body(ApiResponse.DELETE("Discount deleted successfully"));
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.NOT_FOUND("Discount with id "+ id + "not found"));
            }
        }catch (Exception e){
            return  ResponseEntity.internalServerError().body(ApiResponse.SERVER_ERROR("Error deleting discount: "+ e.getMessage()));
        }

    }

}
