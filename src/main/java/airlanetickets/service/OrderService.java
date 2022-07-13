package airlanetickets.service;


import airlanetickets.model.Order;
import airlanetickets.model.Ticket;

import java.util.List;
import java.util.Optional;

public interface OrderService {

   Order save(Order order);


     Order findById(Long id);

   Order delete(Long id, Ticket ticekt);

   List<Order> findAll();



}
