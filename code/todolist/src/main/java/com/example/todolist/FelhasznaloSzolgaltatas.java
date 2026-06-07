package com.example.todolist;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class FelhasznaloSzolgaltatas implements UserDetailsService {

    private final FelhasznaloRepository felhasznaloRepository;

    public FelhasznaloSzolgaltatas(FelhasznaloRepository felhasznaloRepository) {
        this.felhasznaloRepository = felhasznaloRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String felhasznalonev) throws UsernameNotFoundException {
        Felhasznalo felhasznalo = felhasznaloRepository.findByFelhasznalonev(felhasznalonev)
                .orElseThrow(() -> new UsernameNotFoundException("Felhasználó nem található: " + felhasznalonev));

        return User.builder()
                .username(felhasznalo.getFelhasznalonev())
                .password(felhasznalo.getJelszo())
                .roles(felhasznalo.getSzerep())
                .build();
    }
}