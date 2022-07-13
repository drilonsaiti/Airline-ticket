package airlanetickets.service;


import airlanetickets.model.enumerations.Role;
import airlanetickets.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User register(String username, String password, String repeatPassword, String name, String surname, String email,Role role);
    User findByUsername(String username);
}
