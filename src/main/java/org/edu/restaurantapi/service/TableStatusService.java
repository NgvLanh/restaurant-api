package org.edu.restaurantapi.service;

import org.edu.restaurantapi.model.TableStatus;
import org.edu.restaurantapi.model.User;
import org.edu.restaurantapi.repository.TableStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
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
        Pageable pageableSorted = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "Id"));
        return tableStatusRepository.findTableStatusByIsDeleteFalse(pageableSorted);
    }

    public TableStatus getTableStatus(Long id) {
        return tableStatusRepository.findById(id).orElse(null);
    }

    public Boolean deleteTableStatus(Long id) {
        return tableStatusRepository.findById(id).map(tableStatus -> {
            tableStatus.setIsDelete(true);
            tableStatusRepository.save(tableStatus);
            return true;
        }).orElse(false);
    }

    // check table name status
    public Boolean tableStatusNameExists(String tableStatus) {
        return tableStatusRepository.findTableStatusByNameAndIsDeleteFalse(tableStatus).isPresent();
    }
}
