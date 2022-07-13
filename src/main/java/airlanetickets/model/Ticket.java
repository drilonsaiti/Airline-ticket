package airlanetickets.model;


import airlanetickets.model.enumerations.TicketStatus;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Data
@Entity
public class Ticket {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateCreated;

    @ManyToOne
    private User user;

    /*@ManyToMany
    private List<Flight> flights;

    @ManyToMany
    private List<Reservation> reservations;

    @ManyToMany
    private List<Payment> payments;*/

    @ManyToMany
    private List<Order> orders;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;


    public Ticket(User user) {
        this.dateCreated = LocalDateTime.now();
        this.user = user;
      /*  this.flights = new ArrayList<>();
        this.reservations = new ArrayList<>();
        this.payments = new ArrayList<>();*/
        this.orders = new ArrayList<>();
        this.status = TicketStatus.CREATED;
    }

    public Ticket() {
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    public void removeOrder(Order order) {
        this.orders.remove(order);
    }


}


