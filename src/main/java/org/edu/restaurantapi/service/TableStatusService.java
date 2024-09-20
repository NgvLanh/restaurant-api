package org.edu.restaurantapi.service;

import org.edu.restaurantapi.model.TableStatus;
import org.edu.restaurantapi.model.User;
import org.edu.restaurantapi.repository.TableStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@PreAuthorize("hasRole('ADMIN')")
public class TableStatusService {

    @Autowired
    private TableStatusRepository tableStatusRepository;

    public TableStatus createTableStatus(TableStatus tableStatus) {
        return tableStatusRepository.save(tableStatus);
    }

    public TableStatus updateTableStatus(Long id, TableStatus tableStatus) {
        return tableStatusRepository.findById(id).map(existingtableStatus -> {
            existingtableStatus.setName(tableStatus.getName() != null
                    ? tableStatus.getName() : existingtableStatus.getName());
            return tableStatusRepository.save(existingtableStatus);
        }).orElse(null);
    }

    public Page<TableStatus> getTableStatuses(Pageable pageable) {
        return tableStatusRepository.findTableStatusByIsDeleteFalse(pageable);
    }

    public TableStatus getTableStatus(Long id) {
        return tableStatusRepository.findById(id).orElse(null);
    }

    public TableStatus deleteTableStatus(Long id) {
        TableStatus tableStatus = tableStatusRepository.findById(id).orElse(null);
        if (tableStatus.getIsDelete()) {
            return null;
        }
        tableStatus.setIsDelete(true);
        return tableStatusRepository.save(tableStatus);
    }

    // check table name status
    public Boolean tableStatusNameExists(User user) {
        return tableStatusRepository.findTableStatusByName(user.getEmail()).isPresent();
    }
}
