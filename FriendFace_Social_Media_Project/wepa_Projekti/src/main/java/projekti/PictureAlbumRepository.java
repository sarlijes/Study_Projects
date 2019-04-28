package projekti;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PictureAlbumRepository extends JpaRepository<PictureAlbum, Long> {

    PictureAlbum getPictureAlbumByOwner(UserAccount owner);

}
