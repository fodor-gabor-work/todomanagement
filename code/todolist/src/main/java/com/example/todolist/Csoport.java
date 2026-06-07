package com.example.todolist;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Csoport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nev;

    @ElementCollection
    private List<String> emailCimek;

    public Long getId() {return id;}
    public void setId(Long id) {this.id=id;}

    public String getNev() {return nev;}
    public void setNev(String nev) {this.nev=nev;}

    public List<String> getEmailCimek() { return  emailCimek;}
    public void setEmailCimek(List<String> emailCimek) {this.emailCimek=emailCimek;}
}
