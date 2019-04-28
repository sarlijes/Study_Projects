package projekti.interact;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import projekti.UserAccount;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    List<FriendRequest> findBySourceUserAccount(UserAccount sourceUserAccount);
    List<FriendRequest> findByTargetUserAccount(UserAccount targetUserAccount);

}

