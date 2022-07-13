package airlanetickets.web;

import airlanetickets.model.Agency;
import airlanetickets.model.Airplane;
import airlanetickets.model.Flight;
import airlanetickets.service.AgencyService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AgencyController {

    private final AgencyService agencyService;

    public AgencyController(AgencyService agencyService) {
        this.agencyService = agencyService;
    }


    @GetMapping("/agencies")
    public String viewAgency(Model model){
        return findPagianted(model,1,"nameOfAgency","asc");
    }

    @GetMapping("/agencies/page/{pageNo}")
    public String findPagianted(Model model,
                            @PathVariable(value = "pageNo") int pageNo,
                            @RequestParam("sortField") String sortFiled,
                            @RequestParam("sortDir") String sortDir) {
        int pageSize = 4;
        Page<Agency> page = this.agencyService.findPaginated(pageNo,pageSize,sortFiled,sortDir);
        List<Agency> agencies = page.getContent();

        model.addAttribute("currentPage",pageNo);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalItems",page.getTotalElements());

        model.addAttribute("sortField",sortFiled);
        model.addAttribute("sortDir",sortDir);
        String reverseSortdir = sortDir.equals("asc") ? "desc" : "asc";
        model.addAttribute("reverseSortDir", reverseSortdir);

        model.addAttribute("agencies", agencies);
        model.addAttribute("title","Agencies");
        model.addAttribute("bodyContent","agencies");

        return "master-template";
    }

    @GetMapping("/agencies/add")
    public String addAgencies(Model model) {
        model.addAttribute("title","Add agency");
        model.addAttribute("bodyContent","add-agency");

        return "master-template";
    }

    @PostMapping("/agencies")
    public String create(@RequestParam String nameOfAgency,
                         @RequestParam String cityOfAgency,
                         @RequestParam String countryOfAgency,
                         @RequestParam int yearOfCreated) {
        this.agencyService.create(nameOfAgency, cityOfAgency, countryOfAgency, yearOfCreated);
        return "redirect:/agencies";

    }


    @PostMapping("/agencies/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam String nameOfAgency,
                         @RequestParam String cityOfAgency,
                         @RequestParam String countryOfAgency,
                         @RequestParam int yearOfCreated) {
        this.agencyService.update(id, nameOfAgency, cityOfAgency, countryOfAgency, yearOfCreated);
        return "redirect:/agencies";
    }

    @GetMapping("/agencies/{id}/edit")
    public String showEdit(@PathVariable Long id,Model model) {
        Agency agency = this.agencyService.findById(id);

        model.addAttribute("agency",agency);
        model.addAttribute("title","Add agency");
        model.addAttribute("bodyContent","add-agency");


        return "master-template";
    }

    @PostMapping("/agencies/{id}/delete")
    public String delete(@PathVariable Long id) {
        this.agencyService.delete(id);
        return "redirect:/agencies";
    }
}
