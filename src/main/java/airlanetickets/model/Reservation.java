package airlanetickets.model;


import airlanetickets.model.enumerations.ClassesType;
import lombok.Data;

import javax.persistence.*;
import javax.transaction.Transactional;

@Data
@Entity
@Transactional
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    String surname;

    String numberOfPassport;

    String numberOfPhone;

    int baggingPrice;

    @Enumerated(value = EnumType.STRING)
    ClassesType classesType;


    public Reservation(String name, String surname, String numberOfPassport,String numberOfPhone,
                       int baggingPrice,ClassesType classesType) {
        this.name = name;
        this.surname = surname;
        this.numberOfPassport = numberOfPassport;
        this.numberOfPhone = numberOfPhone;
        this.baggingPrice = baggingPrice;
        this.classesType = classesType;
    }

    public Reservation() {
    }

}
