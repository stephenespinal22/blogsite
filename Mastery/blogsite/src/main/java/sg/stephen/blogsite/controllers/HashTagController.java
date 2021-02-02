/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.stephen.blogsite.controllers;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import sg.stephen.blogsite.dtos.BlogPost;
import sg.stephen.blogsite.dtos.HashTag;
import sg.stephen.blogsite.services.BlogPostService;
import sg.stephen.blogsite.services.HashTagService;

/**
 *
 * @author stephenespinal
 */
@Controller
public class HashTagController {

    private HashTagService hashTagService;

    private BlogPostService blogPostService;

    @Autowired
    public HashTagController(HashTagService hashTagService, BlogPostService blogPostService) {
        this.hashTagService = hashTagService;
        this.blogPostService = blogPostService;
    }

    @GetMapping("hashTags")
    public String loadPage(Model model) {
        List<BlogPost> unPublishedblogPostList = blogPostService.readAllBlogPostsByIsPublished(false);
        model.addAttribute("amountToApprove", unPublishedblogPostList.size());
        List<HashTag> hashTagList = hashTagService.readAllHashTags();

        model.addAttribute("hashTagList", hashTagList);

        return "hashTags";
    }

    @PostMapping("addNewHashTag")
    public String addNewHashTag(@Valid HashTag hashTag, BindingResult result, Model model) {

        hashTagService.createHashTag(hashTag);

        //tell spring to redirect user to mapping locations
        return "redirect:/hashTags";
    }

    @PostMapping("editHashTag")
    public String editHashTag(@Valid HashTag hashTag, BindingResult result, Model model) {

        hashTagService.updateHashTag(hashTag);

        //tell spring to redirect user to mapping locations
        return "redirect:/hashTags";
    }

    @PostMapping("deleteHashTag")
    public String deleteHashTag(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("hashTagId"));
        hashTagService.deleteHashTag(id);

        return "redirect:/hashTags";
    }
}
