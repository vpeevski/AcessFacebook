package hello;

import java.util.ArrayList;
import java.util.List;

import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.PagingParameters;
import org.springframework.social.facebook.api.Post;
import org.springframework.social.facebook.api.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HelloController {

    private Facebook facebook;
    private ConnectionRepository connectionRepository;

    public HelloController(Facebook facebook, ConnectionRepository connectionRepository) {
        this.facebook = facebook;
        this.connectionRepository = connectionRepository;
    }

    @GetMapping
    public String helloFacebook(Model model) {
        if (connectionRepository.findPrimaryConnection(Facebook.class) == null) {
            return "redirect:/connect/facebook";
        }

       
        String [] fields = { "id","name","birthday","email","location","hometown","gender","first_name","last_name"};
        User user = facebook.fetchObject("me", User.class, fields);
        model.addAttribute("name", user.getName());
        //String name=user.getName();
//        PagedList<Post> feed = facebook.feedOperations().getFeed();
//        model.addAttribute("feed", feed);
        List<Post> allPosts = new ArrayList<>();
        PagedList<Post> feed = facebook.feedOperations().getFeed(new PagingParameters(50, 0, null, null));
        while (feed.size() > 0) {
        	 allPosts.addAll(feed);
             PagingParameters page =  feed.getNextPage();// 
             feed = facebook.feedOperations().getFeed(page);
             
         }
        model.addAttribute("feed", allPosts);
        return "hello";
    }

}
