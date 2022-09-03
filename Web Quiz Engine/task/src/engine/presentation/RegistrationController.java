package engine.presentation;

import engine.business.User;
import engine.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
public class RegistrationController {
    @Autowired
    UserRepository userRepo;
    @Autowired
    PasswordEncoder encoder;

    @PostMapping("/api/register")
    public void register(@Valid @RequestBody User user) {
        if (user.getEmail() != null && userRepo.findUserByEmail(user.getEmail()) != null) {
            System.out.println(user.getEmail());
            System.out.println(userRepo.findAll());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        user.setQuizzesList(new ArrayList<>());
        user.setPassword(encoder.encode(user.getPassword()));
        userRepo.save(user);
    }
}