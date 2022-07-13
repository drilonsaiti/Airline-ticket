package airlanetickets.service.impl;

import airlanetickets.model.*;
import airlanetickets.model.enumerations.TicketStatus;
import airlanetickets.model.exceptions.InvalidTicketIdException;
import airlanetickets.model.exceptions.ShoppingCartNotFoundException;
import airlanetickets.repository.*;
import airlanetickets.service.OrderService;
import airlanetickets.service.TicketService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class TicketImpl implements TicketService {

    private final TicketRepository ticketRepository;

    private final UserRepository userRepository;

    private final FlightRepository flightRepository;

    private final ReservationRepository reservationRepository;

    private final PaymentRepository paymentRepository;

    private final OrderService orderRepository;

    public TicketImpl(TicketRepository ticketRepository, UserRepository userRepository,
                      FlightRepository flightRepository, ReservationRepository reservationRepository,
                      PaymentRepository paymentRepository, OrderService orderRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.flightRepository = flightRepository;
        this.reservationRepository = reservationRepository;
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }



    @Override
    public List<Order> listAllOrderInShoppingCart(Long cartId) {
        if (!this.ticketRepository.findById(cartId).isPresent())
            throw new ShoppingCartNotFoundException(cartId);
        return this.ticketRepository.findById(cartId).get().getOrders().stream()
                .sorted(Comparator.comparing(Order::getDateCreated).reversed()).collect(Collectors.toList());
    }


    @Override
    public void deleteByTicketAndOrderID(Ticket ticket, Long id) {

    }

    @Override
    public void delete(String username) {
        Ticket ticket = this.getActiveTicketCart(username);
        this.ticketRepository.delete(ticket);
    }

    @Override
    public Ticket getActiveTicketCart(String username) {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow();

        return this.ticketRepository
                .findByUserAndStatus(user, TicketStatus.CREATED)
                .orElseGet(() -> {
                    Ticket cart = new Ticket(user);
                    return this.ticketRepository.save(cart);
                });
    }

    @Override
    @Transactional
    public Ticket addTicketToTicketCart(String username, Long flightID,Long reservationID,Long paymentID) throws Exception {
        Ticket ticket = this.getActiveTicketCart(username);

        Flight flight = this.flightRepository.findById(flightID).orElseThrow();
        Reservation reservation = this.reservationRepository.findById(reservationID).orElseThrow();
        Payment payment = this.paymentRepository.findById(paymentID).orElseThrow();

        Order order = new Order(flight,reservation,payment);

        this.orderRepository.save(order);

        ticket.getOrders().add(order);


        return this.ticketRepository.save(ticket);
    }

    @Override
    public Ticket findById(Long id) {
        Ticket ticket = this.ticketRepository.findById(id).orElseThrow(InvalidTicketIdException::new);

        return ticket;
    }

    @Override
    public Ticket deleteById(Long id) {
        Ticket ticket = this.findById(id);

        this.ticketRepository.delete(ticket);

        return ticket;
    }
}
