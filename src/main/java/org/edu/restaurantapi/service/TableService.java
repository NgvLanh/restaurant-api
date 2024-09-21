package org.edu.restaurantapi.service;

import org.edu.restaurantapi.model.Table;
import org.edu.restaurantapi.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@PreAuthorize("hasRole('ADMIN')")
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
            return tableRepository.save(existingTable);
        }).orElse(null);
    }

    public Page<Table> getTables(Pageable pageable) {
        return tableRepository.findTableByIsDeleteFalse(pageable);
    }

    public Table getTable(Long id) {
        return tableRepository.findById(id).orElse(null);
    }

    public Table deleteTable(Long id) {
        Table table = tableRepository.findById(id).orElse(null);
        if (table != null && table.getIsDelete()) {
            return null;
        }
        if (table != null) {
            table.setIsDelete(true);
            return tableRepository.save(table);
        }
        return null;
    }
}
