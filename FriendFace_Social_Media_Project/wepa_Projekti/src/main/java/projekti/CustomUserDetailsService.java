package projekti;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        if (userAccountRepository.findByUserName(userName) == null) {
            UserAccount dummy = new UserAccount();
            dummy.setUserName(userName);
            userAccountRepository.save(dummy);
        }

        UserAccount userAccount = userAccountRepository.findByUserName(userName);
        if (userAccount == null) {
            throw new UsernameNotFoundException("No such user: " + userName);
        }

        return new org.springframework.security.core.userdetails.User(
                userAccount.getUserName(),
                userAccount.getPassWord(),
                true,
                true,
                true,
                true,
                Arrays.asList(new SimpleGrantedAuthority("USER")));
    }
}
