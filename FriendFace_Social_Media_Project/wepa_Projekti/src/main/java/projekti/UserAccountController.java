package projekti;

import java.time.LocalDateTime;
import static java.time.LocalDateTime.now;
import java.util.*;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import projekti.interact.Comment;
import projekti.interact.CommentRepository;
import projekti.interact.FriendRequest;
import projekti.interact.FriendRequestService;
import projekti.interact.CommentService;
import projekti.interact.FriendRequestController;

@Controller

public class UserAccountController {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private UserAccountRepository userAccountRepository;
    @Autowired
    private PictureAlbumService pictureAlbumService;
    @Autowired
    private PictureAlbumController pictureAlbumController;
    @Autowired
    private MessageService messageService;
    @Autowired
    private PictureService pictureService;
    @Autowired
    private PictureRepository pictureRepository;
    @Autowired
    private CommentService commentService;
    @Autowired
    private FriendRequestService friendRequestService;
    @Autowired
    private FriendRequestController friendRequestController;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("/homepage")
    public String homepage() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        System.out.println("username: kirjautunut: " + username);
        return "redirect:/profile/" + userAccountService.getUserAccountByUserName(username).getProfileCode();
    }

    @GetMapping("/profile/{profileCode}")
    public String showProfilePage(Model model, @PathVariable String profileCode) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();
        UserAccount userAccount = userAccountService.getUserAccountByProfileCode(profileCode);

        Boolean hasPendingSent = friendRequestService.hasPendingSentFriendRequests(userAccount);
        Boolean hasPendingRecieved = friendRequestService.hasPendingRecievedFriendRequests(userAccount);

        List<FriendRequest> sentFriendRequests = friendRequestService.getSentFriendRequestsByUserAccount(userAccount);
        List<FriendRequest> recievedFriendRequests = friendRequestService.getRecievedFriendRequestsByUserAccount(userAccount);

        List<UserAccount> allUserAccounts = userAccountService.getAllUserAccounts();
        List<UserAccount> possibleFriends = friendRequestController.findPossibleFriends(allUserAccounts, userAccount);
        Boolean hasFriends = friendRequestController.hasFriends(userAccount);

        model.addAttribute("logged", userAccountService.getUserAccountByUserName(loggedInUsername));
        model.addAttribute("loggedUserNameUpperCase", userAccountService.getUserAccountByUserName(loggedInUsername).getUserName().toUpperCase());
        model.addAttribute("userAccount", userAccount);
        model.addAttribute("allUserAccounts", possibleFriends);
        model.addAttribute("hasFriends", hasFriends);

        Page<Message> recievedmessages = messageService.findMax25messages(userAccount.getId());

        List<Picture> pictures = pictureAlbumService.getPictureAlbumByOwner(userAccount).getPictures();

        for (Message m : recievedmessages) {
            m.setMessageComments(commentService.getCommentsByInteractableId(m.getId()));
        }
        for (Picture p : pictures) {
            p.setPictureComments(commentService.getCommentsByInteractableId(p.getId()));
        }

        model.addAttribute("pictures", pictures);
        model.addAttribute("recievedmessages", recievedmessages);
        model.addAttribute("sentFriendRequests", sentFriendRequests);
        model.addAttribute("recievedFriendRequests", recievedFriendRequests);
        model.addAttribute("hasPendingSentFriendRequests", hasPendingSent);
        model.addAttribute("hasPendingRecievedFriendRequests", hasPendingRecieved);

        Long profilePicId = pictureService.getProfilePictureId(userAccount);
        model.addAttribute("profilePicId", profilePicId);
        return "profile";
    }

    @PostMapping("/signup")
    public String create(@Valid @ModelAttribute UserAccount userAccount, BindingResult bindingResult) {
        if (!userAccount.getPassWord().equals(userAccount.getConfirmPassWord())) {
            bindingResult.rejectValue("confirmPassWord", "error.userName", "Passwords don't match");
            return "signup";
        }
        if (bindingResult.hasErrors()) {
            return "signup";
        }
        try {
            if (userAccountService.userNameisValid(userAccount.getUserName())) {
                if (userAccountService.profileCodeisValid(userAccount.getProfileCode())) {
                    userAccount.setPassWord(passwordEncoder.encode(userAccount.getPassWord()));

                    userAccount.setConfirmPassWord(passwordEncoder.encode(userAccount.getConfirmPassWord()));
                    userAccountService.addUserAccount(userAccount);
                    pictureAlbumService.addPictureAlbum(userAccountService.getIdByProfileCode(userAccount.getProfileCode()));
                    return "redirect:/login";
                }
            } else if (!userAccountService.profileCodeisValid(userAccount.getProfileCode())) {
                bindingResult.rejectValue("profileCode", "error.profileCode", "Profile code already in use");
                return "signup";
            } else if (!userAccountService.profileCodeisValid(userAccount.getUserName())) {
                bindingResult.rejectValue("userName", "error.userName", "Username already in use");
                return "signup";
            }
            return "signup";
        } catch (DataIntegrityViolationException ex) {
            bindingResult.rejectValue("userName", "error.user", "Error occured, please try again.");
            return "signup";
        }
    }

    @GetMapping("/signup")
    public String showSignUpPage(@ModelAttribute UserAccount userAccount) {
        return "signup";
    }
}
