package airlanetickets.service;


import airlanetickets.model.User;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

public interface AuthService {

    User login(String username, String password);

    Optional<User> findByUsername(String username);

    void delete(String username);

    User update(String username, String usernameOfUser, String nameOfUser, String surnameOfUser, String emailOfUser);

}
