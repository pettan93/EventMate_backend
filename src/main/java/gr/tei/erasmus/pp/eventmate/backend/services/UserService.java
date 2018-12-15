package gr.tei.erasmus.pp.eventmate.backend.services;

import gr.tei.erasmus.pp.eventmate.backend.DTOs.UserDTO;
import gr.tei.erasmus.pp.eventmate.backend.models.Event;
import gr.tei.erasmus.pp.eventmate.backend.models.Task;
import gr.tei.erasmus.pp.eventmate.backend.models.User;
import gr.tei.erasmus.pp.eventmate.backend.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PermissionService permissionService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       PermissionService permissionService,
                       ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.permissionService = permissionService;
        this.modelMapper = modelMapper;
    }

    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    public void register(List<User> users) {
        for (User user : users) {
            register(user);
        }
    }

    public User getUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        return userOptional.orElse(null);

    }

    public List<User> getUsersById(List<Long> ids) {
        return userRepository.findAllById(ids);
    }


    public UserDTO convertToDto(User user) {

        UserDTO userDto = modelMapper.map(user, UserDTO.class);


        Optional<List> userEvents = permissionService.getModels(Event.class, user);
        Optional<List> userTasks = permissionService.getModels(Task.class, user);

        userDto.setEventCount(userEvents.map(List::size).orElse(0));
        userDto.setTaskCount(userTasks.map(List::size).orElse(0));


        return userDto;

    }

    public User convertToEntity(UserDTO userDTO) {

        return null;
    }


}
