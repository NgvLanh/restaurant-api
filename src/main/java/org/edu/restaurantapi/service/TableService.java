package org.edu.restaurantapi.service;

import org.edu.restaurantapi.model.Table;
import org.edu.restaurantapi.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class TableService {

    @Autowired
    private TableRepository tableRepository;

    public Table createTable(Table table) {
        return tableRepository.save(table);
    }

    public Table updateTable(Long id, Table table) {
        return tableRepository.findById(id).map(existingTable -> {
            existingTable.setZone(table.getZone() != null
                    ? table.getZone() : existingTable.getZone());
            existingTable.setTableStatus(table.getTableStatus() != null
                    ? table.getTableStatus() : existingTable.getTableStatus());
            existingTable.setSeats(table.getSeats() != null
                    ? table.getSeats() : existingTable.getSeats());
            existingTable.setBranch(table.getBranch() != null
                    ? table.getBranch() : existingTable.getBranch());
            return tableRepository.save(existingTable);
        }).orElse(null);
    }

    public Page<Table> getTables(Pageable pageable) {
        Pageable pageableSorted = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "Id"));
        return tableRepository.findTableByIsDeleteFalse(pageableSorted);
    }

    public Table getTable(Long id) {
        return tableRepository.findById(id).orElse(null);
    }

    public Boolean deleteTable(Long id) {
        return tableRepository.findById(id).map(table -> {
            table.setIsDelete(true);
            tableRepository.save(table);
            return true;
        }).orElse(false);
    }

    public Boolean numberExists(Integer number) {
        return tableRepository.existsByNumberAndIsDeleteFalse(number);
    }
    public Boolean existsByNumber(Integer number) {
        return tableRepository.existsByNumberAndIsDeleteFalse(number);
    }
}
