package com.example.todolist;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSzolgaltatas {
    private final JavaMailSender mailSender;
    public EmailSzolgaltatas( JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    public  void kuldEmail(String cimzett, String teendoCim, String teendoLeiras ){
        SimpleMailMessage uzenet = new SimpleMailMessage();
        uzenet.setTo(cimzett);
        uzenet.setSubject("Új teendő érkezett: " + teendoCim);
        uzenet.setText("Kedves Felhasználó!\n\n Új teendőt kaptál:\n\n Cím: " + teendoCim + "\nLeírás: " + teendoLeiras + "\n\nÜdvözlettel,\n Teendőlista");
        mailSender.send(uzenet);
    }
}
