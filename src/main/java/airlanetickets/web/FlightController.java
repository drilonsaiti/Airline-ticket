package airlanetickets.web;


import airlanetickets.model.*;
import airlanetickets.model.enumerations.Role;
import airlanetickets.service.*;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class FlightController {

    private final FlightService flightService;

    private final AgencyService agencyService;

    private final AirplaneService airplaneService;

    private final UserService userService;


    private final OrderService orderService;



    public FlightController(FlightService flightService, AgencyService agencyService, AirplaneService airplaneService, UserService userService, OrderService orderService) {
        this.flightService = flightService;
        this.agencyService = agencyService;
        this.airplaneService = airplaneService;
        this.userService = userService;

        this.orderService = orderService;
    }

    @GetMapping("/flights")
    public String getFlights(@RequestParam(required = false) String error,
                                  Model model,HttpServletRequest req){

        String keyword = null;
        return findPagianted(keyword,keyword,keyword,1,model,req);
    }

    @GetMapping("/flights/page/{pageNo}")
    public String findPagianted(@RequestParam(required = false) String fromSearch,
                             @RequestParam(required = false) String toSearch,
                             @RequestParam(required = false) String deptSearch,
                                @PathVariable(value = "pageNo") int pageNo,
                             Model model,HttpServletRequest req){

        String username = req.getRemoteUser();
        Page<Flight> page = null;
        int pageSize = 15;
        if (username != null && !username.isEmpty()) {
             page = this.flightService.findPaginated(pageNo, pageSize, fromSearch, toSearch, deptSearch, username);
        }else{
            page = this.flightService.findPaginated(pageNo, pageSize, fromSearch, toSearch, deptSearch, "null");
        }

        System.out.println("DEPT SEARCH " + deptSearch);

        List<Flight> flights = page.getContent();
        List<Order> orders = this.orderService.findAll();

        model.addAttribute("currentPage",pageNo);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("fromSearch", fromSearch);
        model.addAttribute("toSearch", toSearch);
        model.addAttribute("deptSearch", deptSearch);
        model.addAttribute("orders", orders);

        model.addAttribute("flights",flights);

        model.addAttribute("title","Flight");
        model.addAttribute("bodyContent","flights");

        return "master-template";
    }

    @GetMapping("/flights/add")
    public String addFlight(Model model ){
        List<Agency> agencies = this.agencyService.listAll();
        List<Airplane> airplanes = this.airplaneService.listAll();


        model.addAttribute("agencies",agencies);
        model.addAttribute("airplanes",airplanes);
        model.addAttribute("title","Add flight");
        model.addAttribute("bodyContent","add-flights");

        return "master-template";
    }

    @PostMapping("/flights")
    public String create(@RequestParam String departureFrom,
                         @RequestParam String departureTo,
                         @RequestParam String departureTime,
                         @RequestParam String arrivalTime,
                         @RequestParam Long agency,
                         @RequestParam Long airplane,
                         @RequestParam String duration,
                         @RequestParam int price,
                         @RequestParam int seats,HttpServletRequest req){
        String username = req.getRemoteUser();
        User user = this.userService.findByUsername(username);
        if(user.getRole() == Role.ROLE_ADMIN) {
            this.flightService.create(departureFrom, departureTo, departureTime, arrivalTime, agency, airplane, duration, price, seats);
            return "redirect:/flights";
        }else{
            return "redirect:/login";

        }

    }
    @PostMapping("/flights/reservation")
    public String reservation(){
        return "redirect:/reservation";
    }
    @PostMapping("/flights/{id}")
    public String update(@PathVariable Long id,
                        @RequestParam String departureFrom,
                         @RequestParam String departureTo,
                         @RequestParam String departureTime,
                         @RequestParam String arrivalTime,
                         @RequestParam Long agency,
                         @RequestParam Long airplane,
                         @RequestParam String duration,
                         @RequestParam int price,
                         @RequestParam int seats,HttpServletRequest req){
        String username = req.getRemoteUser();
        User user = this.userService.findByUsername(username);
        if(user.getRole() == Role.ROLE_ADMIN) {
            this.flightService.update(id, departureFrom, departureTo, departureTime, arrivalTime, agency, airplane, duration, price, seats);
            return "redirect:/flights";
        }else{
            return "redirect:/login";

        }

    }

    @GetMapping("/flights/{id}/edit")
    public String showEdit(@PathVariable Long id,Model model,HttpServletRequest req) {
        String username = req.getRemoteUser();
        User user = this.userService.findByUsername(username);
        if(user.getRole() == Role.ROLE_ADMIN) {
            Flight flight = this.flightService.findById(id);

            List<Agency> agencies = this.agencyService.listAll();
            List<Airplane> airplanes = this.airplaneService.listAll();

            model.addAttribute("flight", flight);
            model.addAttribute("agencies", agencies);
            model.addAttribute("airplanes", airplanes);
            model.addAttribute("title", "Add flight");
            model.addAttribute("bodyContent", "add-flights");

            return "master-template";
        }else{
            return "redirect:/login";
        }
    }

    @PostMapping("/flights/{id}/delete")
    public String delete(@PathVariable Long id,HttpServletRequest req) {
        String username = req.getRemoteUser();
        User user = this.userService.findByUsername(username);
        if(user.getRole() == Role.ROLE_ADMIN) {
            this.flightService.delete(id);
            return "redirect:/flights";
        }else{
            return "redirect:/login";
        }
    }


}
