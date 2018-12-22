package gr.tei.erasmus.pp.eventmate.backend.services;

import gr.tei.erasmus.pp.eventmate.backend.DTOs.UserDTO;
import gr.tei.erasmus.pp.eventmate.backend.DTOs.UserPublicDTO;
import gr.tei.erasmus.pp.eventmate.backend.models.User;
import gr.tei.erasmus.pp.eventmate.backend.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final EventService eventService;
    private final TaskService taskService;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       ModelMapper modelMapper,
                       EventService eventService,
                       TaskService taskService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.eventService = eventService;
        this.taskService = taskService;
    }


    public Boolean isUserNameUsed(String userName){
        return userRepository.findByUserName(userName.strip()) != null;
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

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public List<User> getUsersById(List<Long> ids) {
        return userRepository.findAllById(ids);
    }


    public UserDTO convertToDto(User user) {

        UserDTO userDto = modelMapper.map(user, UserDTO.class);


        var userEvents = eventService.getUserEvents(user);
        var oragnizedEvents = userEvents
                .stream()
                .filter(event -> event.getEventOwner() != null && event.getEventOwner().equals(user))
                .collect(Collectors.toList());

        userDto.setAttendedEvents(userEvents.size());
        userDto.setOrganizedEvents(oragnizedEvents.size());

        return userDto;

    }

    public User convertToEntity(UserDTO userDTO) {

        User user = modelMapper.map(userDTO, User.class);

        return user;
    }

    public UserPublicDTO convertToPublicDto(User user) {

        return modelMapper.map(user, UserPublicDTO.class);
    }



}
