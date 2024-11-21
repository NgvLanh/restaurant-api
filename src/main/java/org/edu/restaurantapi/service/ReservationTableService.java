package org.edu.restaurantapi.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReservationTableService {

    @PersistenceContext
    private EntityManager entityManager;


    @Transactional
    public List<Object[]> getAllReservationTables() {
        String sql = "SELECT rt FROM reservation_table rt";
        Query query = entityManager.createNativeQuery(sql);
        return query.getResultList();
    }
}
