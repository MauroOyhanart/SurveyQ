package com.maurooyhanart.surveyq.backend.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public User createUser(UserRequest userRequest) {
        String userEmail = userRequest.getEmail();
        Optional<User> optionalUser = userRepository.findByEmail(userEmail);
        if (optionalUser.isPresent()) {
            String errorText = "User with email " + userEmail + " already exists";
            throw new IllegalArgumentException(errorText);
        }

        User user = new User();
        user.setEmail(userEmail);

        return userRepository.save(user);
    }

}
