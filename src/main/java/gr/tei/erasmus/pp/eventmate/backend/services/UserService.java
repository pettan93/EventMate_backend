package gr.tei.erasmus.pp.eventmate.backend.services;

import gr.tei.erasmus.pp.eventmate.backend.models.User;
import gr.tei.erasmus.pp.eventmate.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public User register(String userName, String email, String password) {

        User newUser = new User();
        newUser.setUserName(userName);
        newUser.setUserName(userName);
        newUser.setPassword(passwordEncoder.encode(password));

        return userRepository.save(newUser);
    }


    public User register(User user) {
        userRepository.save(user);
        return user;
    }

    public void register(List<User> users) {
        for (User user : users) {
            register(user);
        }
    }


}
