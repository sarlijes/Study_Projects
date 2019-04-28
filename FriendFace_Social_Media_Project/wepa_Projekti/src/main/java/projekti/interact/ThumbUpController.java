package projekti.interact;

import java.time.LocalDateTime;
import static java.time.LocalDateTime.now;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projekti.MessageService;
import projekti.UserAccount;
import projekti.UserAccountService;
import projekti.Interactable;
import projekti.Message;
import projekti.MessageRepository;
import projekti.PictureService;
import projekti.Picture;

@Controller
public class ThumbUpController {

    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private PictureService pictureService;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ThumbUpService thumbUpService;

    @PostMapping("/profile/{profileCode}/picture/{id}/addThumbUp")
    public String addPictureThumbUp(@PathVariable String profileCode, @PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();
        Picture picture = pictureService.getOne(id);

        UserAccount giver = userAccountService.getUserAccountByUserName(loggedInUsername);

        ThumbUp thumbUp = new ThumbUp(giver, picture);

        for (ThumbUp t : picture.getPictureThumbUps()) {
            if (t.getGiver() == giver) {
                picture.getPictureThumbUps().remove(t);
                thumbUpService.deleteThumbUpById(t.getId());
                return "redirect:/profile/" + profileCode;
            }
        }
        picture.getPictureThumbUps().add(thumbUp);
        thumbUpService.addThumbUp(thumbUp);
        return "redirect:/profile/" + profileCode;
    }

    @PostMapping("/profile/{profileCode}/message/{id}/addThumbUp")
    public String addMessageThumbUp(@PathVariable String profileCode, @PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();
        UserAccount giver = userAccountService.getUserAccountByUserName(loggedInUsername);
        Message message = messageRepository.getOne(id);
        ThumbUp thumbUp = new ThumbUp(giver, message);
        for (ThumbUp t : message.getMessageThumbUps()) {
            if (t.getGiver() == giver) {
                message.getMessageThumbUps().remove(t);
                thumbUpService.deleteThumbUpById(t.getId());
                return "redirect:/profile/" + profileCode;
            }
        }
        message.getMessageThumbUps().add(thumbUp);
        thumbUpService.addThumbUp(thumbUp);
        return "redirect:/profile/" + profileCode;
    }

}
