package projekti.interact;

import java.io.IOException;
import java.time.LocalDateTime;
import static java.time.LocalDateTime.now;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projekti.PictureAlbumService;
import projekti.UserAccount;
import projekti.UserAccountRepository;
import projekti.UserAccountService;

@Controller
public class FriendRequestController {

    @Autowired
    private FriendRequestService friendRequestService;
    @Autowired
    private UserAccountService userAccountService;

    @PostMapping("/friendrequest/reject/{id}")
    public String reject(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();
        String authProfileCode = userAccountService.getUserAccountByUserName(loggedInUsername).getProfileCode();
        friendRequestService.rejectFriendRequest(friendRequestService.getFriendRequestById(id));
        return "redirect:/profile/" + authProfileCode;
    }

    @PostMapping("/friendrequest/approve/{id}")
    public String approve(@PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();
        String authProfileCode = userAccountService.getUserAccountByUserName(loggedInUsername).getProfileCode();
        friendRequestService.acceptFriendRequest(friendRequestService.getFriendRequestById(id));
        return "redirect:/profile/" + authProfileCode;
    }

    @Transactional
    @PostMapping("/profile/{profileCode}/sendFriendRequest")
    public String sendFriendRequest(@PathVariable String profileCode, @RequestParam Long chosenId, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();
        UserAccount sourceUserAccount = userAccountService.getUserAccountByUserName(loggedInUsername);
        UserAccount targetUserAccout = userAccountService.getUserAccountById(chosenId);
        FriendRequest friendRequest = new FriendRequest(sourceUserAccount, targetUserAccout, now(), false, true);
        friendRequestService.addFriendRequest(friendRequest);
        return "redirect:/profile/" + profileCode;
    }

    public List<UserAccount> findPossibleFriends(List<UserAccount> allUserAccounts, UserAccount userAccount) {
        List<UserAccount> possibleFriends = new ArrayList<>();
        possibleFriends.addAll(allUserAccounts);
        possibleFriends.remove(userAccount);
        for (UserAccount uAll : allUserAccounts) {
            for (FriendRequest f : uAll.getRecievedFriendRequests()) {
                if (f.getSourceUserAccount() == userAccount || f.getTargetUserAccount() == userAccount) {
                    possibleFriends.remove(uAll);
                }
            }
            for (FriendRequest f : uAll.getSentFriendRequests()) {
                if (f.getSourceUserAccount() == userAccount || f.getTargetUserAccount() == userAccount) {
                    possibleFriends.remove(uAll);
                }
            }
        }
        return possibleFriends;
    }

    public Boolean hasFriends(UserAccount u) {
        Boolean hasFriends = false;
        for (FriendRequest f : u.getRecievedFriendRequests()) {
            if (f.getAccepted() == true) {
                hasFriends = true;
            }
        }
        for (FriendRequest f : u.getSentFriendRequests()) {
            if (f.getAccepted() == true) {
                hasFriends = true;
            }
        }
        return hasFriends;
    }
}

// https://stackoverflow.com/questions/49051830/how-to-extract-the-selected-value-from-a-datalist-with-thymeleaf-and-spring
