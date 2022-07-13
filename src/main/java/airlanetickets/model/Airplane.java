package airlanetickets.model;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.transaction.Transactional;

@Data
@Entity
@Transactional

public class Airplane {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String nameOfAirplane;

    int yearOfCreated;

    int total_seats;

    public Airplane(String nameOfAirplane, int yearOfCreated, int total_seats) {
        this.nameOfAirplane = nameOfAirplane;
        this.yearOfCreated = yearOfCreated;
        this.total_seats = total_seats;
    }

    public Airplane() {
    }

}
