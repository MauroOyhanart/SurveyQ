package com.maurooyhanart.surveyq.session.user;

import com.maurooyhanart.surveyq.session.user.request.UserRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/session/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userResponses = users.stream()
                .map(UserDTO::toUserDto)
                .toList();
        return ResponseEntity.ok(userResponses);
    }

    @PostMapping("/session/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(UserDTO.toUserDto(userService.createUser(userRequest)));
    }
}
