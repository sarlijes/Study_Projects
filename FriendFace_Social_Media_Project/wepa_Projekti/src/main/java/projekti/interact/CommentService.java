package projekti.interact;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projekti.Message;
import projekti.MessageRepository;
import projekti.UserAccount;
import projekti.UserAccountRepository;
import projekti.UserAccountService;

@Service
public class CommentService {

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private CommentController commentController;

    @Autowired
    private CommentRepository commentRepository;

    @Transactional
    public void addComment(Comment comment) {
        commentRepository.save(comment);
    }

    public List<Comment> getCommentsByInteractableId(Long id) {
        return commentRepository.findMax10CommentsByInteractableId(id);
    }

    public Page<Comment> getMax10CommentsByInteractableId(Long id) {
        Pageable pageable = PageRequest.of(0, 3, Sort.by("commentTimeStamp").descending());
        Page<Comment> max10Comments = commentRepository.findAllByInteractableId(pageable, id);
        return max10Comments;
    }

}
