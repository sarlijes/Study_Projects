package projekti;

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
public class PictureAlbumController {

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private PictureAlbumService pictureAlbumService;

    public String createPictureAlbum(Long id) {
        pictureAlbumService.addPictureAlbum(id);
        return "redirect:/";
    }
}
