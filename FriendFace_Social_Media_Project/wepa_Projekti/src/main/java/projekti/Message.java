package projekti;

import java.time.LocalDateTime;
import java.util.*;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;
import projekti.interact.Comment;
import projekti.interact.ThumbUp;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Message extends Interactable {

    @ManyToOne
    private UserAccount sender;
    @ManyToOne
    private UserAccount reciever;
    private LocalDateTime messageTimeStamp;
    @Lob
    @Column(name = "CONTENT", length = 1500)
    private String content;
    @OneToMany
    private List<ThumbUp> messageThumbUps = new ArrayList<>();
    @OneToMany
    private List<Comment> messageComments = new ArrayList<>();
}
