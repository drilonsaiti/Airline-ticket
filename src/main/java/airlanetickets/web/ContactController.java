package airlanetickets.web;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContactController {

    @GetMapping("/contact")
    public String getContact(Model model){
        model.addAttribute("title","Contact");
        model.addAttribute("bodyContent","contact");

        return "master-template";
    }
}
