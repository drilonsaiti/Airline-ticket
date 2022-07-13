package airlanetickets.web;


import airlanetickets.model.*;
import airlanetickets.service.FlightService;
import airlanetickets.service.PaymentSercvice;
import airlanetickets.service.ReservationService;
import airlanetickets.service.TicketService;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class PaymentController {

    private final PaymentSercvice paymentSercvice;

    private final FlightService flightService;

    private final ReservationService reservationService;

    private final TicketService ticketService;


    public PaymentController(PaymentSercvice paymentSercvice, FlightService flightService, ReservationService reservationService, TicketService ticketService) {
        this.paymentSercvice = paymentSercvice;
        this.flightService = flightService;
        this.reservationService = reservationService;
        this.ticketService = ticketService;
    }

    @GetMapping("/payment")
    public String getReservation(@Param("idFlight")Long idFlights,@Param("idReservation")Long idReservation ,Model model, HttpServletRequest req){

        String str =  (String) req.getSession().getAttribute("idFlight") == null ? "NULL" : "NOT NULL";
        Reservation reservation = new Reservation();
        Flight flight = new Flight();
        double price = 0.0;
        if(idFlights != null || str.equals("NOT NULL")) {
            Long idFlight = idFlights != null ? Long.valueOf(idFlights) : Long.valueOf((String) req.getSession().getAttribute("idFlight"));
            flight = this.flightService.findById(idFlight);

            Long idReservations = idReservation != null ? Long.valueOf(idReservation) : (Long) req.getSession().getAttribute("idReservation") != null ? (Long) req.getSession().getAttribute("idReservation") : -1;


            if (idReservations != -1) {
                reservation = this.reservationService.findById(idReservations);
            }


            String bagginPrice = req.getSession().getAttribute("bagging") != null ? (String) req.getSession().getAttribute("bagging") : "0";
            int baggingPrice = Integer.parseInt(bagginPrice);

            price = flight.getFinalPrice(reservation.getClassesType()) + baggingPrice;
            flight.setFinalPrice(price);
        }


        List<Integer> month = IntStream.range(1,13).boxed().collect(Collectors.toList());
        List<Integer> year = IntStream.range(2021,2034).boxed().collect(Collectors.toList());


        model.addAttribute("flight",flight);
        model.addAttribute("reservation",reservation);
        model.addAttribute("price",price);
        model.addAttribute("monthValid",month);
        model.addAttribute("yearValid",year);

        model.addAttribute("title","Payment");
        model.addAttribute("bodyContent","payment");

        return "master-template";

    }

    @PostMapping("/payment")
    public String createPayment(HttpServletRequest req,
                                @Param("idFlights")Long idFlights,
                                @Param("idReservations")Long idReservations,
                                    @RequestParam String fullname,
                                    @RequestParam String cardNumber,
                                    @RequestParam String ccv2,
                                    @RequestParam String monthfValid,
                                    @RequestParam String yearOfValid) throws Exception {

        Long id  = this.paymentSercvice.create(fullname,cardNumber,ccv2,monthfValid,yearOfValid).getCode();
        req.getSession().setAttribute("idPayment",id);

        try {
            String username = req.getRemoteUser();

            Long idFlight = idFlights != null ? Long.valueOf(idFlights) : Long.valueOf((String) req.getSession().getAttribute("idFlight"));
            Long idReservation = idReservations != null ? Long.valueOf(idReservations) : (Long) req.getSession().getAttribute("idReservation");

            Ticket ticket = this.ticketService.addTicketToTicketCart(username,idFlight,idReservation,id);
            flightService.updateSeats(idFlight,1);
            return "redirect:/ticket-cart";
        } catch (RuntimeException exception) {
            return "redirect:/ticket-cart?error=" + exception.getMessage();
        }
    }
}
