package airlanetickets.model;

import lombok.Data;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Data
@Entity
@Transactional
@Table(name = "order_table")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private LocalDateTime dateCreated;

    @ManyToOne
    Flight flight;

    @ManyToOne
    Reservation reservation;

    @ManyToOne
    Payment payment;



    public Order(Flight flight, Reservation reservation, Payment payment) {
        this.dateCreated = LocalDateTime.now();
        this.flight = flight;
        this.reservation = reservation;
        this.payment = payment;
    }

    public Order() {
    }

    public boolean canCancaled(String date){
        String data = date.substring(0,10);
        String time = date.substring(11,16);

        String [] partsDate = data.split("-");
        int day = Integer.parseInt(partsDate[0]);
        int month = Integer.parseInt(partsDate[1]);
        int year = Integer.parseInt(partsDate[2]);


        LocalDateTime now = LocalDateTime.now();

        String [] partsTime = time.split(":");

        int hour = Integer.parseInt(partsTime[0]);
        int minute = Integer.parseInt(partsTime[1]);

        if (day > now.getDayOfMonth() && month >= now.getMonth().getValue() && year == now.getYear()) {
            return true;
        }

        return false;
    }

    public boolean canDownload(String date){
        String data = date.substring(0,10);
        String time = date.substring(11,16);

        String [] partsDate = data.split("-");
        int day = Integer.parseInt(partsDate[0]);
        int month = Integer.parseInt(partsDate[1]);
        int year = Integer.parseInt(partsDate[2]);


        LocalDateTime now = LocalDateTime.now();

        String [] partsTime = time.split(":");

        int hour = Integer.parseInt(partsTime[0]);
        int minute = Integer.parseInt(partsTime[1]);

        System.out.println("DAY " + day + " " + now.getDayOfMonth() );
        System.out.println("Month " + month + " " + now.getMonth().getValue());
        System.out.println("YEAR " + year + " " + now.getYear());

        if (day >= now.getDayOfMonth() && month >= now.getMonth().getValue() && year == now.getYear()) {
            System.out.println("TRUE");
            return true;
        }
        System.out.println("FALSE");

        return false;
    }



    public String getDateFromFlight(){
        return flight.getDeparatureTime();
    }
}
