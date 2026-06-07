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

    public TodoController(TodoRepository todoRepository, EmailSzolgaltatas emailSzolgaltatas, GoogleNaptarSzolgaltatas googleNaptarSzolgaltatas){
        this.todoRepository =todoRepository;
        this.emailSzolgaltatas = emailSzolgaltatas;
        this.googleNaptarSzolgaltatas = googleNaptarSzolgaltatas;
    }

    // Teendők lekérése a meglévő listából
    @GetMapping("/")
    public String lista(Model model){
        model.addAttribute("todok", todoRepository.findAll());
        return "index";
    }

    // Új teendő hozzáadása
    @PostMapping("/hozzaad")
    public String hozzaad(String cim, String leiras, String email,
                          @RequestParam(required = false) String hatarido) {
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
}
