package airlanetickets.service;

import airlanetickets.model.enumerations.ClassesType;
import airlanetickets.model.Reservation;

public interface ReservationService {

    Reservation create(String name, String surname, String numberOfPass,String numberPhone,int baggingPrice,ClassesType type);

    Long getIdReservation();

    Reservation findById(Long id);
}
