package exercise.controller;

import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller
public class Controller {
    @GetMapping("/")
    public String index(){
        return "index.html";
    }
}
