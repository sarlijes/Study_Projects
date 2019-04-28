package projekti;

import java.time.LocalDateTime;
import static java.time.LocalDateTime.now;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller

public class MessageController {

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private MessageService messageService;

    @PostMapping("/profile/{profileCode}/message")
    public String addMessage(@PathVariable String profileCode, @RequestParam String content) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();

        LocalDateTime dateTime = now();
        UserAccount reciever = userAccountService.getUserAccountByProfileCode(profileCode);
        UserAccount sender = userAccountService.getUserAccountByUserName(loggedInUsername);
        messageService.addWallMessage(sender, reciever, dateTime, content);
        return "redirect:/profile/" + profileCode;
    }
}

