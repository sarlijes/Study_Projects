package course;

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
public class CourseRegistrationController {

    @Autowired
    private RegistrationRepository allData;

    @GetMapping("/")
    public String list(Model model) {
        model.addAttribute("list", allData.findAll());
        return "index";
    }

    @PostMapping("/")
    public String create(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String email, @RequestParam String phone) {
        allData.save(new Registration(firstName, lastName, email, phone));
        return "redirect:/";
    }
}
