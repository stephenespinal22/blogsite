/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.stephen.blogsite.controllers;

/**
 *
 * @author stephenespinal
 */
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import sg.stephen.blogsite.daos.RoleDao;
import sg.stephen.blogsite.daos.UserDao;
import sg.stephen.blogsite.dtos.BlogPost;
import sg.stephen.blogsite.services.BlogPostService;

@Controller
public class HomeController {

    private BlogPostService blogPostService;

    @Autowired
    public HomeController(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }

    @GetMapping({"/", "/home"})
    public String displayHomePage(Model model) {
        List<BlogPost> unPublishedblogPostList = blogPostService.readAllBlogPostsByIsPublished(false);
        List<BlogPost> publishedblogPostList = blogPostService.readAllBlogPostsByIsPublished(true);
        model.addAttribute("amountToApprove", unPublishedblogPostList.size());

        model.addAttribute("publishedblogPostList", publishedblogPostList);
        return "home";
    }
}
