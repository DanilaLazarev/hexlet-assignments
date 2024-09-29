package exercise.controller;

import exercise.daytime.Daytime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

// BEGIN

@RestController
public  class WelcomeController {

    @Autowired
    private Daytime daytime;

    @GetMapping(path = "/welcome")
    public String welcome(){
        return daytime.getName().equals("day")? "It is day now! Welcome to Spring!" : "It is night now! Welcome to Spring!";
    }
}
// END
