package com.example.todolist;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cim;
    private String leiras;
    private boolean kesz;

    // Getterek és setterek
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCim() { return cim; }
    public void setCim(String cim) { this.cim = cim; }

    public String getLeiras() { return leiras; }
    public void setLeiras(String leiras) { this.leiras = leiras; }

    public boolean isKesz() { return kesz; }
    public void setKesz(boolean kesz) { this.kesz = kesz; }
}