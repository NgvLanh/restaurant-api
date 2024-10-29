package org.edu.restaurantapi.service;

import org.edu.restaurantapi.model.TableStatus;
import org.edu.restaurantapi.model.TableStatus;
import org.edu.restaurantapi.model.User;
import org.edu.restaurantapi.repository.TableStatusRepository;
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
    private TableStatusRepository repository;

    public Page<TableStatus> gets(String name, Pageable pageable) {
        Pageable pageableSorted = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        return repository.findByNameContaining(name, pageableSorted);
    }

    public TableStatus create(TableStatus request) {
        return repository.save(request);
    }

    public TableStatus update(Long id, TableStatus request) {
        return repository.findById(id).map(b -> {
            b.setName(request.getName() != null ? request.getName() : b.getName());
            b.setColorCode(request.getColorCode() != null ? request.getColorCode() : b.getColorCode());
            return repository.save(b);
        }).orElse(null);
    }

    public Boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        }
        return !repository.existsById(id);
    }

    public Boolean findByName(String name) {
        return repository.findByName(name) != null;
    }

    public Boolean findByNameAndIdNot(String name, Long id) {
        return repository.findByNameAndIdNot(name, id) != null;
    }
}
