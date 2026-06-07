package com.example.todolist;

import jakarta.persistence.*;

@Entity
public class Felhasznalo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String felhasznalonev;
    private String jelszo;
    private String szerep;

    public Long getId() {return id;}
    public void setId(Long id) {this.id=id;}

    public String getFelhasznalonev() {return felhasznalonev;}
    public void setFelhasznalonev(String felhasznalonev) {this.felhasznalonev=felhasznalonev;}

    public String getJelszo() {return jelszo;}
    public void setJelszo(String jelszo) {this.jelszo=jelszo;}

    public String getSzerep() {return szerep;}
    public void setSzerep(String szerep) {this.szerep = szerep;}
}
