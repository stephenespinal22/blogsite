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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sg.stephen.blogsite.daos.UserDao;
import sg.stephen.blogsite.dtos.BlogPost;
import sg.stephen.blogsite.dtos.Category;
import sg.stephen.blogsite.dtos.HashTag;
import sg.stephen.blogsite.dtos.User;
import sg.stephen.blogsite.services.BlogPostService;
import sg.stephen.blogsite.services.CategoryService;
import sg.stephen.blogsite.services.HashTagService;

/**
 *
 * @author stephenespinal
 */
@Controller
public class ManagePostsController {

    private static String UPLOADED_FOLDER = "src/main/resources/static/blogCoverImages/";

    private BlogPostService blogPostService;
    private HashTagService hashTagService;
    private CategoryService catService;

    @Autowired
    UserDao users;

    @Autowired
    public ManagePostsController(BlogPostService blogPostService, HashTagService hashTagService, CategoryService catService) {
        this.blogPostService = blogPostService;
        this.hashTagService = hashTagService;
        this.catService = catService;
    }

    @GetMapping("managePosts")
    public String loadPage(Model model, Principal principal) {

        List<BlogPost> unPublishedblogPostList = blogPostService.readAllBlogPostsByIsPublished(false);
        model.addAttribute("amountToApprove", unPublishedblogPostList.size());
        List<BlogPost> blogPostList = blogPostService.readAllBlogPostsByIsPublished(true);

        model.addAttribute("blogPostList", blogPostList);

        List<Category> categoryList = catService.readAllCategorys();

        model.addAttribute("categoryList", categoryList);

        return "managePosts";
    }

    @PostMapping("editBlogPostManage")
    public String editBlogPostManage(@Valid BlogPost blogPost, BindingResult result, Model model, HttpServletRequest request,
            @RequestParam("fileEdit") MultipartFile file, RedirectAttributes redirectAttributes) {

        String[] categoryIds = request.getParameterValues("categories");
        List<Category> categories = new ArrayList<>();

        if (categoryIds != null) {
            for (String categoryId : categoryIds) {
                categories.add(catService.readCategoryById(Integer.parseInt(categoryId)));
            }
        }

        String content = request.getParameter("blogContent");

        Pattern pattern = Pattern.compile("(?<=[\\s>])#(\\d*[A-Za-z_0-9]*)\\b(?!!)");

        Matcher m1 = pattern.matcher(content);

        List<HashTag> hashTags = new ArrayList<>();
        List<String> hashTagIds = new ArrayList<>();

        while (m1.find()) {
            HashTag newTag = new HashTag();
            newTag.setHashTagName(m1.group().substring(1));
            newTag = hashTagService.createHashTag(newTag);
            hashTags.add(hashTagService.readHashTagByName(newTag.getHashTagName()));
            hashTagIds.add(hashTagService.readHashTagByName(newTag.getHashTagName()).getHashTagId() + "");
        }

        //setDate for post
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm a");
        String text = date.format(formatter);
        blogPost.setPostDate(text);

        User user = users.getUserByUsername(request.getParameter("userName"));
        blogPost.setUser(user);
        blogPost.setHashTags(hashTags);
        blogPost.setCategories(categories);
        blogPost.setIsPublished(false);
        blogPostService.updateBlogPost(blogPost, hashTagIds, categoryIds);

        if (!file.isEmpty()) {

            try {

                // Get the file and save it somewhere
                byte[] bytes = file.getBytes();
                Path path = Paths.get(UPLOADED_FOLDER + "featuredImage" + blogPost.getBlogId() + ".jpg");
                Files.write(path, bytes);

                String pathString = path.toString();
                String fixedPath = pathString.substring(25);

                blogPostService.addImage(fixedPath, blogPost.getBlogId());

                redirectAttributes.addFlashAttribute("message",
                        "You successfully uploaded '" + file.getOriginalFilename() + "'");

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
        
        return "redirect:/managePosts";
    }

    @PostMapping("deleteBlogPostManage")
    @Transactional
    public String deleteBlogPostManage(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("blogId"));
        try {
            Path path = Paths.get(UPLOADED_FOLDER + "featuredImage" + id + ".jpg");
            Files.deleteIfExists(path);
        } catch (IOException e) {

        }

        blogPostService.deleteBlogPost(id);

        return "redirect:/managePosts";
    }
}
