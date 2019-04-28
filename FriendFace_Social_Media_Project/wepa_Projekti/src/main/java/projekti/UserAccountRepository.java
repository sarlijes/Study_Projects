package projekti;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    UserAccount getUserAccountByProfileCode(String profileCode);
    UserAccount getUserAccountByUserName(String userName);
    UserAccount findByUserName(String userName);
    UserAccount getUserAccountById(Long id);
}
