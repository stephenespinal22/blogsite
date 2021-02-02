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
import java.security.Principal;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import sg.stephen.blogsite.daos.UserDao;
import sg.stephen.blogsite.dtos.BlogPost;
import sg.stephen.blogsite.dtos.Comment;
import sg.stephen.blogsite.services.BlogPostService;
import sg.stephen.blogsite.services.CommentService;

@Controller
public class ContentController {

    private BlogPostService blogPostService;
    private CommentService commentService;

    @Autowired
    UserDao users;

    @Autowired
    public ContentController(BlogPostService blogPostService, CommentService commentService) {
        this.blogPostService = blogPostService;
        this.commentService = commentService;
    }

    @GetMapping("/content")
    public String displayContentPage(Model model) {
        List<BlogPost> unPublishedblogPostList = blogPostService.readAllBlogPostsByIsPublished(false);
        List<BlogPost> publishedblogPostList = blogPostService.readAllBlogPostsByIsPublished(true);
        model.addAttribute("amountToApprove", unPublishedblogPostList.size());

        model.addAttribute("publishedblogPostList", publishedblogPostList);
        return "content";
    }

    @PostMapping("/contentSearch")
    public String searchByContentPage(Model model, HttpServletRequest request) {
        List<BlogPost> unPublishedblogPostList = blogPostService.readAllBlogPostsByIsPublished(false);
        List<BlogPost> publishedblogPostList = blogPostService.readAllBlogPostsByIsPublished(true);
        
        if (!request.getParameter("searchTerm").isEmpty()) {
        publishedblogPostList = blogPostService.readAllPublishedBlogPostsByHashTagId(request.getParameter("searchTerm"),true);
        }
        
        model.addAttribute("amountToApprove", unPublishedblogPostList.size());

        model.addAttribute("publishedblogPostList", publishedblogPostList);
        
        
        return "content";
    }

    @GetMapping("/post")
    public String postDisplay(Model model, Integer id) {
        List<BlogPost> unPublishedblogPostList = blogPostService.readAllBlogPostsByIsPublished(false);
        model.addAttribute("amountToApprove", unPublishedblogPostList.size());

        BlogPost blogPost = blogPostService.readBlogPostById(id);

        model.addAttribute("blogPost", blogPost);

        List<Comment> commentList = commentService.readCommenstByBlogId(id);

        model.addAttribute("commentList", commentList);

        return "post";
    }

    @PostMapping("addComment")
    public String addComment(@Valid Comment comment, HttpServletRequest request) {

        int id = Integer.parseInt(request.getParameter("blogId"));
        BlogPost blogPost = blogPostService.readBlogPostById(id);

        comment.setUserName(request.getParameter("userName"));

        comment.setBlogPost(blogPost);

        commentService.createComment(comment);

        //tell spring to redirect user to mapping locations
        return "redirect:/post?id=" + blogPost.getBlogId() + "#endPage";
    }

    @PostMapping("deleteComment")
    public String deleteComment(HttpServletRequest request) {

        int id = Integer.parseInt(request.getParameter("blogId"));
        BlogPost blogPost = blogPostService.readBlogPostById(id);

        int commentId = Integer.parseInt(request.getParameter("commentId"));

        commentService.deleteComment(commentId);

        return "redirect:/post?id=" + blogPost.getBlogId() + "#endPage";
    }
}
