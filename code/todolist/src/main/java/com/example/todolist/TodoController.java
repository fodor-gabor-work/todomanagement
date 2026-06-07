package com.example.todolist;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TodoController {
    // Dependence injection
    private final TodoRepository todoRepository;
    private final EmailSzolgaltatas emailSzolgaltatas;
    private final GoogleNaptarSzolgaltatas googleNaptarSzolgaltatas;
    private final CsoportRepository csoportRepository;

    public TodoController(TodoRepository todoRepository, EmailSzolgaltatas emailSzolgaltatas, GoogleNaptarSzolgaltatas googleNaptarSzolgaltatas, CsoportRepository csoportRepository){
        this.todoRepository =todoRepository;
        this.emailSzolgaltatas = emailSzolgaltatas;
        this.googleNaptarSzolgaltatas = googleNaptarSzolgaltatas;
        this.csoportRepository = csoportRepository;
    }

    // Teendők lekérése a meglévő listából
    @GetMapping("/")
    public String lista(Model model){
        model.addAttribute("todok", todoRepository.findAll());
        model.addAttribute("csoportok", csoportRepository.findAll());
        return "index";
    }

    // Új teendő hozzáadása
    @PostMapping("/hozzaad")
    public String hozzaad(String cim, String leiras, String email,
                          @RequestParam(required = false) String hatarido,
                          jakarta.servlet.http.HttpServletRequest request) {
        Todo todo = new Todo();
        todo.setCim(cim);
        todo.setLeiras(leiras);

        if(hatarido != null && !hatarido.isEmpty()){
            todo.setHatarido(java.time.LocalDateTime.parse(hatarido));
        }
        todoRepository.save(todo);

        if (email != null && !email.isEmpty()) {
            emailSzolgaltatas.kuldEmail(email, cim, leiras);
        }

        // Csoport email küldés
        String csoportId = request.getParameter("csoportId");
        if (csoportId != null && !csoportId.isEmpty()){
            Csoport csoport = csoportRepository.findById(Long.parseLong(csoportId)).orElse(null);
            if(csoport != null){
                for(String emailCim: csoport.getEmailCimek()){
                    emailSzolgaltatas.kuldEmail(emailCim.trim(),cim,leiras);
                }
            }
        }
        try {
            googleNaptarSzolgaltatas.addNaptarEsemeny(cim, leiras,todo.getHatarido());
        } catch (Exception e) {
            System.out.println("Naptár hiba: " + e.getMessage());
        }

        return "redirect:/";
    }

    // Teendő törlése
    @GetMapping("/torol/{id}")
    public String torol(@PathVariable Long id){
        todoRepository.deleteById(id);
        return "redirect:/";
    }

    // Teendő elvégzésének dokumentálása
    @GetMapping("/kesz/{id}")
    public String kesz(@PathVariable Long id){
        Todo todo = todoRepository.findById(id).orElseThrow();
        todo.setKesz(true);
        todoRepository.save(todo);
        return "redirect:/";
    }

    @PostMapping("/csoport/hozzaad")
    public String csoportHozzaad(String nev, String emailek){
        Csoport csoport = new Csoport();
        csoport.setNev(nev);
        csoport.setEmailCimek(java.util.Arrays.asList(emailek.split(",")));
        csoportRepository.save(csoport);
        return "redirect:/";
    }

    @GetMapping("/csoport/torol/{id}")
    public String csoportTorol(@PathVariable Long id){
        csoportRepository.deleteById(id);
        return "redirect:/";
    }
}
