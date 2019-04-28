package projekti;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;
import static org.fluentlenium.core.filter.FilterConstructor.withText;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import projekti.*;
import projekti.interact.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class FluentleniumTest extends org.fluentlenium.adapter.junit.FluentTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private UserAccountRepository userAccountRepository;
    @Autowired
    private UserAccountController userAccountController;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private PictureService pictureService;
    @Autowired
    private MessageController messageController;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private MessageService messageService;
    @Autowired
    private ThumbUpController thumbUpController;
    @Autowired
    private ThumbUpRepository thumbUpRepository;
    @Autowired
    private ThumbUpService thumbUpService;
    @Autowired
    private CommentController commentController;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentService commentService;

    @Test
    public void canCreateUserAndLogIn() {
    }

}
/*
    @Test
    public void canCreateUserAndLogIn() {
        int userAccountRepositorySize = userAccountRepository.findAll().size();
        UserAccount valtteri = new UserAccount("vale", "pw1245", "pw1245", "Valtteri", "Bottas", "vale77", null, null, null);
        userAccountRepository.save(valtteri);

        assertTrue(userAccountService.getAllUserAccounts().size() == userAccountRepositorySize + 1);

        goTo("http://localhost:" + port);
        find("#usernameinput").fill().with("vale");
        find("#passwordinput").fill().with("pw1245");

        find("form").first().submit();
//        assertTrue(pageSource().contains("vale"));
    }

                
    <form action="/movies" method="POST">
       
    Name: <input type="text" name="name" id="name"/>
    
                <input type="submit"/>            
            </form>
    
    find("#name").fill().with("Uuno Epsanjassa");
    
     <form th:action="@{/login}" method='POST'>
                                                            <div class="form-group">
                                                                <label for="username">Username</label>
                                                                <input type="text" name='username' class="form-control" id="usernameinput"
                                                                       aria-describedby="emailHelp" placeholder="...">
                                                            </div>
                                                            <div class="form-group">
                                                                <label for="password">Password</label>
                                                                <input type="password" name='password' class="form-control" id="passwordinput"
                                                                       placeholder="...">
                                                            </div>
                                                            <button type="submit" class="btn btn-primary" style=" background-color:cadetblue;">Log in</button>
                                                        </form>
    
     }*/
