package projekti.interact;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;
import projekti.Interactable;
import projekti.UserAccount;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Comment extends AbstractPersistable<Long>{

    @ManyToOne
    private UserAccount writer;
    @ManyToOne
    private Interactable interactable;
    private LocalDateTime commentTimeStamp;
    @Lob
    @Column(name = "CONTENT", length = 1500)
    private String content;

}
