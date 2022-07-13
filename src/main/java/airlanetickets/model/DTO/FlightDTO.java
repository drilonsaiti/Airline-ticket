package airlanetickets.model.DTO;

import airlanetickets.model.Agency;
import airlanetickets.model.Airplane;
import airlanetickets.model.enumerations.ClassesType;
import lombok.Data;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

public class FlightDTO {

        String fromLocation;
        String toLocation;
        String deparatureTime;
        String arrival_time;
        String duration;
        int total_seats;
        int price;
        double finalPrice;
        Agency agency;
        Airplane airplane;

        public FlightDTO( String from_location, String to_location, String deparature_time,
                       String arrival_time, String duration, int total_seats, int price, Agency agency, Airplane airplane) {
            this.fromLocation = from_location;
            this.toLocation = to_location;
            this.deparatureTime = deparature_time;
            this.arrival_time = arrival_time;
            this.duration = duration;
            this.total_seats = total_seats;
            this.price = price;
            this.agency = agency;
            this.airplane = airplane;
            finalPrice = 0;
        }

        public FlightDTO() {
        }


}
