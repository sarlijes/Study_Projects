package wexford.wexford;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.*;
import java.util.Scanner;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

@Controller
public class CarController {

    @Autowired
    private CarRepository carRepository;

    public void readCarFile() {

        Scanner s = new Scanner(System.in);
        // tiedosto:
        // 0    1    2    3     4                 5   6
        // 1,Nissan,350Z,2003,3C3CFFCR8CT442142,Red,22369.91
        try {
            Files.lines(Paths.get("car_data.csv"))
                    .skip(1)
                    .map(row -> row.split(","))
                    .map(a -> create(a[1], a[2], a[4], a[5], a[3], a[6]));
        } catch (Exception e) {
            System.out.println("Error in reading file: " + e.getMessage());
        }
    }

    @PostMapping("/")
    public String create(String make, String model, String vin, String color, String year, String price) {
        carRepository.save(new Car(make, model, vin, color, year, price));
        System.out.println("luotu");
        return "index";

    }

    @GetMapping("/")
    public String list(Model model) {
        model.addAttribute("cars", carRepository.findAll());
        return "index";
    }

}
