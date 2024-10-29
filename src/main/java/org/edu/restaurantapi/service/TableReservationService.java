package org.edu.restaurantapi.service;

import lombok.extern.slf4j.Slf4j;
import org.edu.restaurantapi.model.TableReservation;
import org.edu.restaurantapi.repository.TableReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TableReservationService {
    @Autowired
    private TableReservationRepository repository;

    public TableReservation create(TableReservation request) {
        return repository.save(request);
    }

    public Page<TableReservation> gets(Long branchId, Pageable pageable) {
        return repository.findByIsDeleteFalseAndReservationBranchId(branchId, pageable);
    }
}
