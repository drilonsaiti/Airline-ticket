package airlanetickets.web;

import airlanetickets.model.Order;
import airlanetickets.model.Ticket;
import airlanetickets.service.OrderService;

import airlanetickets.service.TicketService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;


@Controller
public class VerificationController {

    private final TicketService ticketService;

    private final OrderService orderService;

    public VerificationController(TicketService ticketService, OrderService orderService) {
        this.ticketService = ticketService;
        this.orderService = orderService;
    }


    @GetMapping("/verification")
    public String getSearch(Model model){
        model.addAttribute("title","Verification");
        model.addAttribute("bodyContent","verificationSearch");

        return "master-template";

    }

    @GetMapping("/verification/search")
    public String getDetails(@RequestParam(required = false) Long idOrder, HttpServletRequest req, Model model){


        if (idOrder != null ) {
            Order order = this.orderService.findById(idOrder);
            int baggingPrice = order.getReservation().getBaggingPrice();

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
            model.addAttribute("order", order);
        }else{
            Order order = new Order();

            model.addAttribute("order", order);
        }

        return "/verification";
    }
}
