package friendface;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller

public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/")
    public String create(@RequestParam String username, @RequestParam String password, @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String profileCode) {
        userRepository.save(new User(username, password, firstName, lastName, profileCode));
        return "redirect:/";
    }

}
