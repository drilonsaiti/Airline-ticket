package airlanetickets.service.impl;

import airlanetickets.model.enumerations.ClassesType;
import airlanetickets.model.Reservation;
import airlanetickets.model.exceptions.InvalidReservationIdException;
import airlanetickets.repository.ReservationRepository;
import airlanetickets.service.ReservationService;
import org.springframework.stereotype.Service;


@Service
public class ReservationImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    Long idReservation;

    public ReservationImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public Reservation create(String name, String surname, String numberOfPass, String numberPhone,int baggingPrice, ClassesType type) {
        Reservation reservation = new Reservation(name,surname,numberOfPass,numberPhone,baggingPrice,type);
        this.idReservation = reservation.getId();
        return reservationRepository.save(reservation);
    }

    @Override
    public Reservation findById(Long id) {
        Reservation reservation = this.reservationRepository.findById(id).orElseThrow(InvalidReservationIdException::new);

        return reservation;
    }

    @Override
    public Long getIdReservation( ) {
        return this.idReservation;
    }
}
