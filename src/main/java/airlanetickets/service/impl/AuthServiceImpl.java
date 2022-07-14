package airlanetickets.service.impl;

import airlanetickets.model.User;
import airlanetickets.model.exceptions.*;
import airlanetickets.repository.UserRepository;

import airlanetickets.service.AuthService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User login(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new InvalidArgumentsException();
        }
        return userRepository.findByUsernameAndPassword(username,
                password).orElseThrow(InvalidUserCredentialsException::new);
    }

    @Override
    public void delete(String username) {
        User user =  this.findByUsername(username).orElseThrow();

        this.userRepository.delete(user);
    }

    @Override
    public User update(String username, String usernameOfUser, String nameOfUser, String surnameOfUser, String emailOfUser) {
        User user =  this.findByUsername(username).orElseThrow();
        if(!Objects.equals(username, usernameOfUser) && this.userRepository.findByUsername(usernameOfUser).isPresent())
            throw new UsernameAlreadyExistsException(username);
        if(!Objects.equals(user.getEmail(), emailOfUser) && this.userRepository.findByEmail(emailOfUser).isPresent())
            throw new EmailAlreadyExistsException(emailOfUser);

        user.setUsername(usernameOfUser);
        user.setName(nameOfUser);
        user.setSurname(surnameOfUser);
        user.setEmail(emailOfUser);

        return this.userRepository.save(user);
    }

    @Override
    @Transactional
    public Optional<User> findByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

}
