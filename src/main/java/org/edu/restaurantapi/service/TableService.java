package org.edu.restaurantapi.service;

import org.edu.restaurantapi.model.Branch;
import org.edu.restaurantapi.model.Table;
import org.edu.restaurantapi.model.Zone;
import org.edu.restaurantapi.repository.BranchRepository;
import org.edu.restaurantapi.repository.TableRepository;
import org.edu.restaurantapi.repository.ZoneRepository;
import org.edu.restaurantapi.request.TableRequest;
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
    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private ZoneRepository zoneRepository;

    public Page<Table> getAllTables(Optional<String> branch, Long zoneId, Pageable pageable) {
        Pageable pageableSorted = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "number"));
        if (zoneId == null || zoneId == 0) {
            return tableRepository.findByBranchIdAndActiveTrue(Long.parseLong(branch.get()), pageableSorted);
        }
        return tableRepository.findByBranchIdAndZoneIdAndActiveTrue(Long.parseLong(branch.get()), zoneId, pageableSorted);
    }

    public Table create(TableRequest request) {
        Integer number;
        Branch branch = branchRepository.findById(request.getBranchId()).get();
        Zone zone = zoneRepository.findById(request.getZoneId()).get();
        if (request.getNumber() == null) {
            try {
                number = tableRepository.findMaxNumberByBranchId(request.getBranchId()) + 1;
            } catch (Exception e) {
                number = 1;
            }
        } else {
            number = request.getNumber();
        }
        Table table = Table.builder()
                .number(number)
                .seats(request.getSeats())
                .branch(branch)
                .zone(zone)
                .build();
        return tableRepository.save(table);
    }

    public Table update(Long id, TableRequest request) {
        Zone zone = zoneRepository.findById(request.getZoneId()).orElse(null);
        return tableRepository.findById(id).map(t -> {
            t.setNumber(request.getNumber() != null ? request.getNumber() : t.getNumber());
            t.setSeats(request.getSeats() != null ? request.getSeats() : t.getSeats());
            t.setZone(zone != null ? zone : t.getZone());
            return tableRepository.save(t);
        }).orElse(null);
    }


    public Table delete(Long id) {
        return tableRepository.findById(id).map(t -> {
            t.setActive(false);
            return tableRepository.save(t);
        }).orElse(null);
    }

    public Boolean findByIsDeleteFalseAndNumberAndBranchId(Integer number, Long branch) {
        return tableRepository.findByNumberAndBranchIdAndActiveTrue(number, branch) != null;
    }

    public List<Table> getTablesByBranchId(Optional<Long> branch, LocalDate date) {
        List<Table> tables = tableRepository.findAllWithReservationsByBranchId(branch.get());
        for (Table table : tables) {
            table.setReservations(
                    table.getReservations().stream()
                            .filter(reservation -> reservation.getBookingDate().equals(date))
                            .filter(reservation -> reservation.getActive())
                            .collect(Collectors.toList())
            );
        }
        return tables;

    }

}
