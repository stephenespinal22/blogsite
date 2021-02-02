/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.stephen.blogsite.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import sg.stephen.blogsite.dtos.BlogPost;
import sg.stephen.blogsite.services.BlogPostService;

/**
 *
 * @author stephenespinal
 */
@Controller
public class ApprovePostsController {

    private static String UPLOADED_FOLDER = "src/main/resources/static/blogCoverImages/";

    private BlogPostService blogPostService;

    @Autowired
    public ApprovePostsController(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }

    @GetMapping("approvePosts")
    public String loadPage(Model model, Principal principal) {
        List<BlogPost> unPublishedblogPostList = blogPostService.readAllBlogPostsByIsPublished(false);

        model.addAttribute("amountToApprove", unPublishedblogPostList.size());
        model.addAttribute("unPublishedblogPostList", unPublishedblogPostList);

        return "approvePosts";
    }

    @PostMapping("approveThisPost")
    public String approveThisPost(HttpServletRequest request) {

        BlogPost blogPost = blogPostService.readBlogPostById(Integer.parseInt(request.getParameter("blogId")));
        blogPostService.updateBlogPostIsPublishedById(blogPost);

        return "redirect:/approvePosts";
    }

    @PostMapping("deleteBlogPostApproval")
    @Transactional
    public String deleteBlogPostApproval(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("blogId"));
        try {
            Path path = Paths.get(UPLOADED_FOLDER + "featuredImage" + id + ".jpg");
            Files.deleteIfExists(path);
        } catch (IOException e) {

        }

        blogPostService.deleteBlogPost(id);

        return "redirect:/approvePosts";
    }
}
