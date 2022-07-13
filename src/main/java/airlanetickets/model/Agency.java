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
public class Agency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String nameOfAgency;

    String country;

    String city;

    int yearOfCreated;

    public Agency(String nameOfAgency, String country, String city, int yearOfCreated) {
        this.nameOfAgency = nameOfAgency;
        this.country = country;
        this.city = city;
        this.yearOfCreated = yearOfCreated;
    }

    public Agency() {
    }
}
