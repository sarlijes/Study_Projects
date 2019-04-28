package projekti.interact;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import projekti.UserAccount;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> getCommentsByInteractableId(Long id);
    Page<Comment> findAllByInteractableId(Pageable pageable, Long id);
    @Query(value = "SELECT * from Comment where INTERACTABLE_ID = :id ORDER BY comment_time_stamp DESC LIMIT 10;",
            nativeQuery = true)
    List<Comment> findMax10CommentsByInteractableId(@Param("id") Long id);

}

