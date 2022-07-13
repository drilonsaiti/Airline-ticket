package airlanetickets.service.impl;

import airlanetickets.model.Airplane;
import airlanetickets.model.Flight;
import airlanetickets.model.exceptions.InvalidAirplaneIdException;
import airlanetickets.repository.AirplaneRepository;
import airlanetickets.service.AirplaneService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AirplaneImpl implements AirplaneService {

    private final AirplaneRepository airplaneRepository;

    public AirplaneImpl(AirplaneRepository airplaneRepository) {
        this.airplaneRepository = airplaneRepository;
    }

    @Override
    public Airplane findById(Long id){

        Airplane airplane = this.airplaneRepository.findById(id).orElseThrow(InvalidAirplaneIdException::new);

        return airplane;
    }

    @Override
    public Airplane delete(Long id) {
        Airplane airplane = this.findById(id);

        this.airplaneRepository.delete(airplane);

        return airplane;
    }

    @Override
    public Airplane create(String nameOfAirplane, int yearOfCreatedPlane, int totalSeatsPlane) {
        Airplane airplane = new Airplane(nameOfAirplane,yearOfCreatedPlane,totalSeatsPlane);

        return this.airplaneRepository.save(airplane);
    }

    @Override
    public Airplane update(Long id, String nameOfAirplane, int yearOfCreatedPlane, int totalSeatsPlane) {
        Airplane airplane = this.findById(id);

        airplane.setNameOfAirplane(nameOfAirplane);
        airplane.setYearOfCreated(yearOfCreatedPlane);
        airplane.setTotal_seats(totalSeatsPlane);

        return this.airplaneRepository.save(airplane);
    }

    @Override
    public List<Airplane> listAll() {
        return this.airplaneRepository.findAll();
    }
}
