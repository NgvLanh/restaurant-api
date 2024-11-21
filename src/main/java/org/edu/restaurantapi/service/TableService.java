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

import java.util.List;
import java.util.Optional;

@Service
public class TableService {

    @Autowired
    private TableRepository repository;

    public Page<Table> getAllTables(Optional<String> branch, Pageable pageable) {
        Pageable pageableSorted = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(), Sort.by(Sort.Direction.ASC, "number"));
        return repository.findByIsDeleteFalseAndBranchId(Long.parseLong(branch.get()), pageableSorted);
    }

    public Table create(Table request) {
        Integer number;
        if (request.getNumber() == null) {
           try {
               number = repository.findMaxNumberByBranchId(request.getBranch().getId()) + 1;
           } catch (Exception e) {
               number = 1;
           }
        } else {
            number = request.getNumber();
        }
        request.setNumber(number);
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

    public List<Table> getTablesByBranchId(Optional<Long> branch, Optional<String> time) {
        return repository.findTableByIsDeleteFalseAndBranchIdOrderByNumber(branch.get()) ;
    }

}
