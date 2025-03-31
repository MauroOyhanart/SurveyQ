package com.maurooyhanart.surveyq.session.application;

import com.maurooyhanart.surveyq.session.user.Role;
import com.maurooyhanart.surveyq.session.user.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            roleRepository.save(new Role("ROLE_ADMIN"));
        }
        if (roleRepository.findByName("ROLE_USER").isEmpty()) {
            roleRepository.save(new Role("ROLE_USER"));
        }
        if (roleRepository.findByName("ROLE_SESSION_MODULE").isEmpty()) {
            roleRepository.save(new Role("ROLE_SESSION_MODULE"));
        }
    }
}
