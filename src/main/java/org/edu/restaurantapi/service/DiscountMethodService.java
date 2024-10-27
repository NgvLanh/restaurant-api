package org.edu.restaurantapi.service;

import org.edu.restaurantapi.model.DiscountMethod;
import org.edu.restaurantapi.repository.DiscountMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class DiscountMethodService {

    @Autowired
    private DiscountMethodRepository repository;

    public Page<DiscountMethod> gets(String name, Pageable pageable) {
        Pageable pageableSorted = PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        return repository.findDiscountMethodByNameContaining(name, pageableSorted);
    }

    public DiscountMethod create(DiscountMethod request) {
        return repository.save(request);
    }

    public DiscountMethod update(Long id, DiscountMethod request) {
        return repository.findById(id).map(c -> {
            c.setName(request.getName() != null ? request.getName() : c.getName());
            return repository.save(c);
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
