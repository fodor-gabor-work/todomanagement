package com.example.todolist;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FelhasznaloRepository extends JpaRepository<Felhasznalo, Long> {
    Optional<Felhasznalo> findByFelhasznalonev(String felhasznalonev);
}