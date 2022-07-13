package airlanetickets.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Controller
@RequestMapping("/logout")
public class LogoutController {

    @GetMapping
    public String logout(HttpServletRequest request) {
        String username = request.getRemoteUser();

        if (!Objects.equals(username, "") && username != null) {
            request.getSession().invalidate();
            return "redirect:/login";
        }else{
            return "access_denied";
        }
    }
}
