package airlanetickets.web;

import airlanetickets.model.Agency;
import airlanetickets.model.Airplane;
import airlanetickets.service.AirplaneService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AirplaneController {

    private final AirplaneService airplaneService;

    public AirplaneController(AirplaneService airplaneService) {
        this.airplaneService = airplaneService;
    }

    @GetMapping("/airplanes")
    public String getAgency(Model model) {
        List<Airplane> airplanes = this.airplaneService.listAll();

        model.addAttribute("airplanes", airplanes);
        model.addAttribute("title","Airplanes");
        model.addAttribute("bodyContent","airplanes");

        return "master-template";

    }

    @GetMapping("/airplanes/add")
    public String addFlight(Model model) {
        model.addAttribute("title","Add airplane");
        model.addAttribute("bodyContent","add-airplane");

        return "master-template";
    }

    @PostMapping("/airplanes")
    public String create(@RequestParam String nameOfAirplane,
                         @RequestParam int yearOfCreatedPlane,
                         @RequestParam int totalSeatsPlane) {
        this.airplaneService.create(nameOfAirplane,yearOfCreatedPlane,totalSeatsPlane);
        return "redirect:/airplanes";

    }


    @PostMapping("/airplanes/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam String nameOfAirplane,
                         @RequestParam int yearOfCreatedPlane,
                         @RequestParam int totalSeatsPlane) {
        this.airplaneService.update(id, nameOfAirplane,yearOfCreatedPlane,totalSeatsPlane);
        return "redirect:/airplanes";
    }

    @GetMapping("/airplanes/{id}/edit")
    public String showEdit(@PathVariable Long id,Model model) {
        Airplane plane = this.airplaneService.findById(id);

        model.addAttribute("plane",plane);
        model.addAttribute("title","Add airplane");
        model.addAttribute("bodyContent","add-airplane");

        return "master-template";
    }

    @PostMapping("/airplanes/{id}/delete")
    public String delete(@PathVariable Long id) {
        this.airplaneService.delete(id);
        return "redirect:/airplanes";
    }
}
