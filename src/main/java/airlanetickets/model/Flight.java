package airlanetickets.model;


import airlanetickets.model.enumerations.ClassesType;
import airlanetickets.service.OrderService;
import lombok.Data;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Data
@Entity
@Transactional

public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;


    String fromLocation;

    String toLocation;

    String deparatureTime;

    String arrival_time;

    String duration;

    int total_seats;

    int price;

    double finalPrice;


    @ManyToOne
    Agency agency;

    @ManyToOne
    Airplane airplane;

    public Flight( String from_location, String to_location, String deparature_time,
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

    public Flight() {
    }

    public double getFinalPrice(ClassesType type){

        if (type == ClassesType.ECONOMY_CLASS){
            return this.price;
        }else if (type == ClassesType.BUSINESS_CLASS){
            return this.price * 8.5;
        }else{
            return  this.price * 4.5;
        }
    }

    public double getFinalPrices(){
        return this.finalPrice;
    }

    public String getToLocation() {
        return toLocation;
    }

    public boolean cantShow(){
        String data = deparatureTime.substring(0,10);
        String time = deparatureTime.substring(11,16);

        String [] partsDate = data.split("-");
        int day = Integer.parseInt(partsDate[0]);
        int month = Integer.parseInt(partsDate[1]);
        int year = Integer.parseInt(partsDate[2]);


        LocalDateTime now = LocalDateTime.now();

        String [] partsTime = time.split(":");

        int hour = Integer.parseInt(partsTime[0]);
        int minute = Integer.parseInt(partsTime[1]);


        int min = now.getHour() - hour;
        int minday = (day-now.getDayOfMonth()) * 24;

        System.out.println(this.fromLocation + " " + this.toLocation + " hour " + hour + " now hour " + now.getHour() + " now.getHour - hour " + min + " minday " + minday);

        if (day >= now.getDayOfMonth() && month >= now.getMonth().getValue() && year == now.getYear() && min+minday > 0 && min+minday >= 4) {
            return true;
        }

        return false;
    }



    public void setTotalSeats(int takeSeats){
        this.total_seats = this.total_seats - takeSeats;
    }

    public void setTotalSeatsPlus(int takeSeats){
        this.total_seats = this.total_seats + takeSeats;
    }



}
