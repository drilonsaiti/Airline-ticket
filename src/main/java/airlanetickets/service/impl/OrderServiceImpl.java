package airlanetickets.service.impl;

import airlanetickets.model.Flight;
import airlanetickets.model.Order;
import airlanetickets.model.Ticket;
import airlanetickets.repository.FlightRepository;
import airlanetickets.repository.OrderRepository;
import airlanetickets.repository.TicketRepository;
import airlanetickets.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final FlightRepository flightRepository;

    private final TicketRepository ticketRepository;


    public OrderServiceImpl(OrderRepository orderRepository, FlightRepository flightRepository, TicketRepository ticketRepository) {
        this.orderRepository = orderRepository;
        this.flightRepository = flightRepository;
        this.ticketRepository = ticketRepository;
    }


    @Override
    @Transactional
    public Order save(Order order) {
        System.out.println("ORDER SIZE " + this.orderRepository.findAll().stream().filter(i -> i.getFlight().getFromLocation().equals(order.getFlight().getFromLocation()) && i.getFlight()
                .getDeparatureTime().equals(order.getFlight().getDeparatureTime()) &&
                i.getReservation().getNumberOfPassport().equals(order.getReservation().getNumberOfPassport()))
                .collect(Collectors.toList()).size());


        if (this.orderRepository.findAll().stream().filter(i -> i.getFlight().getFromLocation().equals(order.getFlight().getFromLocation()) && i.getFlight()
        .getDeparatureTime().equals(order.getFlight().getDeparatureTime()) &&
                i.getReservation().getNumberOfPassport().equals(order.getReservation().getNumberOfPassport()))
                .collect(Collectors.toList()).size() > 0){
            this.orderRepository.delete(order);
        }
        return this.orderRepository.save(order);
    }

    @Override

    public Order findById(Long id) {
        return this.orderRepository.findById(id).orElseThrow();
    }


    @Override
    @Transactional
    public Order delete(Long id,Ticket ticket) {
        Order order = this.findById(id);
        order.getFlight().setTotalSeatsPlus(1);
        ticket.removeOrder(order);
        this.orderRepository.delete(order);

        return order;
    }

    @Override
    public List<Order> findAll() {
        return this.orderRepository.findAll();
    }

}
