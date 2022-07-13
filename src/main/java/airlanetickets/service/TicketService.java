package airlanetickets.service;

import airlanetickets.model.*;

import java.util.List;

public interface TicketService {

   /* public List<Flight> listAllFlightInShoppingCart(Long cartId) throws Exception;

    public List<Reservation> listAllReservationInShoppingCart(Long cartId) throws Exception;

    public List<Payment> listAllPaymentsInShoppingCart(Long cartId) throws Exception;*/


    Ticket getActiveTicketCart(String username);

    Ticket addTicketToTicketCart(String username, Long productId,Long reservationID,Long paymentID)throws Exception;

    public Ticket deleteById(Long id);

    public List<Order> listAllOrderInShoppingCart(Long cartId);

    Ticket findById(Long id);

    void deleteByTicketAndOrderID(Ticket ticket,Long id);

    void delete(String username);

    }
