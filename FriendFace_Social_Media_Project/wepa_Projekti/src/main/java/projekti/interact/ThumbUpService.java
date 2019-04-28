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
import projekti.*;

@Service
public class ThumbUpService {

    @Autowired
    private ThumbUpRepository thumbUpRepository;

    @Transactional
    public void addThumbUp(ThumbUp thumbUp) {
        thumbUpRepository.save(thumbUp);
    }

    public List<ThumbUp> getThumbUpsByInteractableId(Long id) {
        return thumbUpRepository.getThumbUpsByInteractableId(id);
    }

    public void deleteThumbUpById(Long id) {
        thumbUpRepository.deleteById(id);
    }

}
