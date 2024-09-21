package org.edu.restaurantapi.service;

import org.edu.restaurantapi.model.Address;
import org.edu.restaurantapi.model.TableReservation;
import org.edu.restaurantapi.repository.TableReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TableReservationService {
    @Autowired
    private TableReservationRepository tableReservationRepository;

    public TableReservation createTableReservation(TableReservation tableReservation){
        return tableReservationRepository.save(tableReservation);
    }

    public Page<TableReservation> getTableReservation(Pageable pageable) {
        return tableReservationRepository.findAll(pageable);
    }



}
