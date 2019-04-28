package projekti.interact;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import projekti.UserAccount;

public interface ThumbUpRepository extends JpaRepository<ThumbUp, Long> {

    List<ThumbUp> getThumbUpsByInteractableId(Long id);

}
