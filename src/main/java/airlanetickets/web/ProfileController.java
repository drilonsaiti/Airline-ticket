package airlanetickets.web;

import airlanetickets.model.User;
import airlanetickets.repository.UserRepository;
import airlanetickets.service.AuthService;
import airlanetickets.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
public class ProfileController {

    private final AuthService userService;

    public ProfileController(AuthService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String getProfile(HttpServletRequest req, Model model){
        String username = req.getRemoteUser();
        System.out.println(username);
        Optional<User> user = this.userService.findByUsername(username);

        model.addAttribute("user",user);
        model.addAttribute("title","Profile");
        model.addAttribute("bodyContent","profile");

        return "master-template";
    }

   @GetMapping("/profile/settings")
    public String getSettings(HttpServletRequest req,Model model){
       String username = req.getRemoteUser();

       Optional<User> user = this.userService.findByUsername(username);

        model.addAttribute("user",user);

       model.addAttribute("title","Settings");
       model.addAttribute("bodyContent","settings");

       return "master-template";

   }

   @PostMapping("/profile/{username}")
    public String update(@PathVariable String username,
                         @RequestParam String usernameOfUser,
                         @RequestParam String nameOfUser,
                         @RequestParam String surnameOfUser,
                         @RequestParam String emailOfUser){
        this.userService.update(username,usernameOfUser, nameOfUser, surnameOfUser, emailOfUser);
        return "redirect:/profile";

    }

   @PostMapping("/user/delete/{username}")
    public String delete(@PathVariable String username){
        this.userService.delete(username);
        return "redirect:/home";
   }
}
