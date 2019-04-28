package projekti;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class PictureController {

    @Autowired
    private PictureService pictureService;

    @Autowired
    private PictureAlbumService pictureAlbumService;

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private PictureRepository pictureRepository;

    @PostMapping("/profile/{profileCode}")
    public String add(@RequestParam("file") MultipartFile file, @PathVariable String profileCode, @RequestParam String caption) throws IOException {
        if (!file.getContentType().equals("image/jpeg")) {
            return "redirect:/profile/" + profileCode;
//        } else if (!file.getContentType().equals("image/gif")) {
//            return "redirect:/profile/" + profileCode;
//        } else if (!file.getContentType().equals("image/png")) {
//            return "redirect:/profile/" + profileCode;
        } 
        UserAccount u = userAccountService.getUserAccountByProfileCode(profileCode);
        
        
        if (u.getPictureAlbum().getPictures().size()  <= 9 && !file.isEmpty()) {
            pictureService.addPicture(file, profileCode, caption, false);
        }
        return "redirect:/profile/" + profileCode;
    }

    @DeleteMapping("/picture/{id}")
    public String delete(@PathVariable Long id) throws IOException {
        pictureRepository.deleteById(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();
        UserAccount u = userAccountService.getUserAccountByUserName(loggedInUsername);
        return "redirect:/profile/" + u.getProfileCode();
    }

    @PostMapping("/picture/{id}")
    public String makeProfilePicture(@PathVariable Long id) throws IOException {
        pictureService.makeProfilePicture(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();
        UserAccount u = userAccountService.getUserAccountByUserName(loggedInUsername);
        return "redirect:/profile/" + u.getProfileCode();
    }

    @GetMapping("/picture/{id}")
    public ResponseEntity<byte[]> viewFile(@PathVariable Long id) {
        Picture pic = pictureRepository.getOne(id);
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(pic.getContentType()));
        headers.setContentLength(pic.getContentLength());

        return new ResponseEntity<>(pic.getContent(), headers, HttpStatus.CREATED);
    }
}