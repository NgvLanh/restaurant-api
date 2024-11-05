package org.edu.restaurantapi.service;

import org.edu.restaurantapi.model.Branch;
import org.edu.restaurantapi.model.Table;
import org.edu.restaurantapi.model.User;
import org.edu.restaurantapi.model.Table;
import org.edu.restaurantapi.repository.TableRepository;
import org.edu.restaurantapi.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TableService {

    @Autowired
    private TableRepository repository;

    public Page<Table> gets(String branch, String number, Pageable pageable) {
        Pageable pageableSorted = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        return repository.findByIsDeleteFalseAndBranchIdAndNumber(Long.parseLong(branch), Integer.parseInt(number), pageableSorted);
    }

    public Table create(Table request) {
        return repository.save(request);
    }

    public Table update(Long id, Table request) {
        return repository.findById(id).map(t -> {
            t.setNumber(request.getNumber() != null ? request.getNumber() : t.getNumber());
            t.setSeats(request.getSeats() != null ? request.getSeats() : t.getSeats());
            t.setZone(request.getZone() != null ? request.getZone() : t.getZone());
            return repository.save(t);
        }).orElse(null);
    }


    public Boolean delete(Long id) {
        return repository.findById(id).map(t -> {
            t.setIsDelete(true);
            repository.save(t);
            return true;
        }).orElse(false);
    }

    public Boolean findByIsDeleteFalseAndNumberAndBranchIs(Integer number, Branch branch) {
        return repository.findByIsDeleteFalseAndNumberAndBranchIs(number, branch) != null;
    }

//    public Boolean findByNameAndIdNot(String name, Long id) {
//        return repository.findByNameAndIdNot(name, id) != null;
//    }

}
