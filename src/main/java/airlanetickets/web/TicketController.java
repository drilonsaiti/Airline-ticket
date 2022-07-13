package airlanetickets.web;


import airlanetickets.model.Flight;
import airlanetickets.model.Order;
import airlanetickets.model.Reservation;
import airlanetickets.model.Ticket;
import airlanetickets.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
public class TicketController {

    private final TicketService ticketService;

    private final FlightService flightService;

    private final ReservationService reservationService;

    private final PaymentSercvice paymentSercvice;

    private final OrderService orderService;


    public TicketController(TicketService ticketService, FlightService flightService,
                            ReservationService reservationService, PaymentSercvice paymentSercvice, OrderService orderService) {
        this.ticketService = ticketService;
        this.flightService = flightService;
        this.reservationService = reservationService;
        this.paymentSercvice = paymentSercvice;
        this.orderService = orderService;
    }

    @GetMapping("/ticket-cart")
    public String getTickerPage(HttpServletRequest req, Model model) throws Exception {
        String username = req.getRemoteUser();

        Ticket ticket = this.ticketService.getActiveTicketCart(username);


        List<Order> list = this.ticketService.listAllOrderInShoppingCart(ticket.getId());
        //Collections.reverse(list);

        model.addAttribute("orders", list);
        model.addAttribute("ticket", ticket);
        model.addAttribute("title", "Ticket cart");
        model.addAttribute("bodyContent", "ticket-cart");

        return "master-template";

    }


    @GetMapping("/ticket-cart/{id}/download")
    public String getDownloadPage(@PathVariable Long id, HttpServletRequest req, Model model) {
        String username = req.getRemoteUser();
        Ticket ticket = this.ticketService.getActiveTicketCart(username);
        int ides = Math.toIntExact(id);

        Order order = this.orderService.findById(id);
       int baggingPrice = order.getReservation().getBaggingPrice();

        model.addAttribute("order", order);
        model.addAttribute("ticket", ticket);

         if(baggingPrice == 28){
             model.addAttribute("bag","/img/bag.png");
             model.addAttribute("bagKG","/img/10kg.png");
        }else if(baggingPrice == 38){
             model.addAttribute("bag","/img/bag.png");
             model.addAttribute("bagKG","/img/20kg.png");
        }else if (baggingPrice == 54){
             model.addAttribute("bag","/img/bag.png");
             model.addAttribute("bagKG","/img/30kg.png");
         }else{
             model.addAttribute("bag","/img/bag.png");
         }

        return "download";
    }

    @PostMapping("/ticket-cart/{id}/canceled")
    public String canceled(@PathVariable Long id, HttpServletRequest req) {
        String username = req.getRemoteUser();
        Ticket ticket = this.ticketService.getActiveTicketCart(username);
        this.orderService.delete(id, ticket);
        return "redirect:/ticket-cart";
    }

    @PostMapping("/ticket-cart/clear")
    public String clear(HttpServletRequest req) {
        String username = req.getRemoteUser();
        this.ticketService.delete(username);
        return "redirect:/ticket-cart";
    }

}
