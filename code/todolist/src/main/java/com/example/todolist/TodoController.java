package com.example.todolist;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TodoController {
    // Dependence injection
    private final TodoRepository todoRepository;
    public TodoController(TodoRepository todoRepository){
        this.todoRepository =todoRepository;
    }
    // Teendők lekérése a meglévő listából
    @GetMapping("/")
    public String lista(Model model){
        model.addAttribute("todok", todoRepository.findAll());
        return "index";
    }

    // Új teendő hozzáadása
    @PostMapping("/hozzaad")
    public String hozzaad(String cim, String leiras){
        Todo todo = new Todo();
        todo.setCim(cim);
        todo.setLeiras(leiras);
        todoRepository.save(todo);
        return "redirect:/";
    }

    // Teendő törlése
    @GetMapping("/torol/{id}")
    public String torol(@PathVariable Long id){
        todoRepository.deleteById(id);
        return "redirect:/";
    }

}
