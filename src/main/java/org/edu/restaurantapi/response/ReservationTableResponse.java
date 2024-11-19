package org.edu.restaurantapi.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.edu.restaurantapi.model.Reservation;
import org.edu.restaurantapi.model.Table;

import java.util.Date;

@Data
@AllArgsConstructor
public class ReservationTableResponse {
    Long id;
    Table table;
    Long tableId;
    Reservation reservation;
    Long reservationId;
    Date createAt;
}