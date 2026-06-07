package com.example.todolist;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdatbazisInicio implements CommandLineRunner {

    private final FelhasznaloRepository felhasznaloRepository;
    private final PasswordEncoder passwordEncoder;

    public AdatbazisInicio(FelhasznaloRepository felhasznaloRepository, PasswordEncoder passwordEncoder) {
        this.felhasznaloRepository = felhasznaloRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (felhasznaloRepository.count() == 0) {
            Felhasznalo admin = new Felhasznalo();
            admin.setFelhasznalonev("admin");
            admin.setJelszo(passwordEncoder.encode("admin123"));
            admin.setSzerep("ADMIN");
            felhasznaloRepository.save(admin);

            Felhasznalo user = new Felhasznalo();
            user.setFelhasznalonev("user");
            user.setJelszo(passwordEncoder.encode("user123"));
            user.setSzerep("USER");
            felhasznaloRepository.save(user);
        }
    }
}