package org.edu.restaurantapi.service;

import org.edu.restaurantapi.model.Branch;
import org.edu.restaurantapi.model.Table;
import org.edu.restaurantapi.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TableService {

    @Autowired
    private TableRepository tableRepository;

    public Page<Table> getAllTables(Optional<String> branch, Pageable pageable) {
        Pageable pageableSorted = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "number"));
        return tableRepository.findByIsDeleteFalseAndBranchId(Long.parseLong(branch.get()), pageableSorted);
    }

    public Table create(Table request) {
        Integer number;
        if (request.getNumber() == null) {
           try {
               number = tableRepository.findMaxNumberByBranchId(request.getBranch().getId()) + 1;
           } catch (Exception e) {
               number = 1;
           }
        } else {
            number = request.getNumber();
        }
        request.setNumber(number);
        return tableRepository.save(request);
    }

    public Table update(Long id, Table request) {
        return tableRepository.findById(id).map(t -> {
            t.setNumber(request.getNumber() != null ? request.getNumber() : t.getNumber());
            t.setSeats(request.getSeats() != null ? request.getSeats() : t.getSeats());
            t.setZone(request.getZone() != null ? request.getZone() : t.getZone());
            return tableRepository.save(t);
        }).orElse(null);
    }


    public Boolean delete(Long id) {
        return tableRepository.findById(id).map(t -> {
            t.setIsDelete(true);
            tableRepository.save(t);
            return true;
        }).orElse(false);
    }

    public Boolean findByIsDeleteFalseAndNumberAndBranchIs(Integer number, Branch branch) {
        return tableRepository.findByIsDeleteFalseAndNumberAndBranchIs(number, branch) != null;
    }

    public List<Table> getTablesByBranchId(Optional<Long> branch, LocalDate date) {
        List<Table> tables = tableRepository.findAllWithReservationsByBranchId(branch.get());
        for (Table table : tables) {
            table.setReservations(
                    table.getReservations().stream()
                            .filter(reservation -> reservation.getBookingDate().equals(date))
                            .filter(reservation -> !reservation.getIsDelete())
                            .collect(Collectors.toList())
            );
        }
        return tables;
    }

}
