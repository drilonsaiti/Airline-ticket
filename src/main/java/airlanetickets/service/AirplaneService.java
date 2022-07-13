package airlanetickets.service;

import airlanetickets.model.Agency;
import airlanetickets.model.Airplane;

import java.util.List;
import java.util.Optional;

public interface AirplaneService {

    List<Airplane> listAll();

    Airplane findById(Long id);

    Airplane delete(Long id);

    Airplane create(String nameOfAirplane,int yearOfCreatedPlane,int totalSeatsPlane);

    Airplane update(Long id,String nameOfAirplane,int yearOfCreatedPlane,int totalSeatsPlane);

}
