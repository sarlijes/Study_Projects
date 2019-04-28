package projekti;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import projekti.interact.Comment;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserAccountRepository userAccountRepository;
    

    @Transactional
    public void addWallMessage(UserAccount sender, UserAccount reciever, LocalDateTime dateTime, String content) {
        Message message = new Message(sender, reciever, dateTime, content, null, null);
        messageRepository.save(message);
    }

    public Page<Message> findMax25messages(Long id) {
        Pageable pageable = PageRequest.of(0, 25, Sort.by("messageTimeStamp").descending());
        Page<Message> recievedMessages = messageRepository.findMax25ByRecieverId(pageable, id);
        return recievedMessages;
    }

    public List<Message> findAllMessages(Long id) {
        List<Message> recievedMessages = messageRepository.findAllByRecieverId(id);
        return recievedMessages;
    }

}
