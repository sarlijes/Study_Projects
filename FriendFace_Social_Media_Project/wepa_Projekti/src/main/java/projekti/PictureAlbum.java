package projekti;

import java.util.*;
import javax.persistence.CascadeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PictureAlbum extends AbstractPersistable<Long> {

    @OneToOne(mappedBy = "pictureAlbum")
    private UserAccount owner;
    
    @OneToMany(mappedBy = "pictureAlbum")
    private List<Picture> pictures = new ArrayList<>();

}
