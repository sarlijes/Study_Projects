package projekti;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PictureService {

    @Autowired
    private PictureRepository pictureRepository;
    @Autowired
    private UserAccountRepository userAccountRepository;
    @Autowired
    private PictureAlbumRepository pictureAlbumRepository;
    @Autowired
    private PictureAlbumService pictureAlbumService;

    public void addPicture(MultipartFile file, String profileCode, String caption, Boolean isProfilePicture) throws IOException {
        String userName = userAccountRepository.getUserAccountByProfileCode(profileCode).getUserName();
        UserAccount userAccount = userAccountRepository.getUserAccountByUserName(userName);
        
        PictureAlbum pictureAlbum = pictureAlbumRepository.getPictureAlbumByOwner(userAccount);
        List<Picture> pictures = pictureAlbum.getPictures();

        
        Picture picture = new Picture();
        picture.setContent(file.getBytes());
        picture.setContentLength(file.getSize());
        picture.setContentType(file.getContentType());
        picture.setName(file.getOriginalFilename());
        picture.setCaption(caption);
        picture.setIsProfilePicture(Boolean.FALSE);
        picture.setPictureAlbum(userAccount.getPictureAlbum());

        pictures.add(picture);
        
        pictureAlbum.setPictures(pictures);
        userAccount.setPictureAlbum(pictureAlbum);
        pictureAlbumRepository.save(pictureAlbum);
        userAccountRepository.save(userAccount);
        pictureRepository.save(picture);
    }

    public void makeProfilePicture(Long id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = auth.getName();
        UserAccount owner = userAccountRepository.getUserAccountByUserName(loggedInUsername);

        PictureAlbum pictureAlbum = pictureAlbumRepository.getPictureAlbumByOwner(owner);
        List<Picture> pictures = pictureAlbum.getPictures();
        System.out.println(owner.getProfileCode());
        System.out.println(id);
        for (Picture pic : pictures) {
            if (pic.getId().equals(id)) {
                pic.setIsProfilePicture(Boolean.TRUE);
            } else {
                pic.setIsProfilePicture(Boolean.FALSE);
            }
        }
        pictureAlbum.setPictures(pictures);
        owner.setPictureAlbum(pictureAlbum);
        pictureAlbumRepository.save(pictureAlbum);
        userAccountRepository.save(owner);
    }

    public Picture getOne(Long id) {
        return pictureRepository.getOne(id);
    }

    Long getProfilePictureId(UserAccount u) {
        Long profilePicId = Long.MIN_VALUE;
        for (Picture pic : pictureAlbumService.getPictureAlbumByOwner(u).getPictures()) {
            if (pic.getIsProfilePicture() == true) {
                profilePicId = pic.getId();

            }
        }
        if (!pictureAlbumService.getPictureAlbumByOwner(u).getPictures().isEmpty()) {
            profilePicId = pictureAlbumService.getPictureAlbumByOwner(u).getPictures().get(0).getId();
        } else {
            profilePicId = null;
        }
        
        return profilePicId;
    }
}
