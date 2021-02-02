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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import sg.stephen.blogsite.daos.UserDao;
import sg.stephen.blogsite.dtos.BlogPost;
import sg.stephen.blogsite.dtos.Category;
import sg.stephen.blogsite.dtos.HashTag;
import sg.stephen.blogsite.dtos.User;
import sg.stephen.blogsite.services.BlogPostService;
import sg.stephen.blogsite.services.CategoryService;
import sg.stephen.blogsite.services.HashTagService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 *
 * @author stephenespinal
 */
@Controller
public class CreateBlogPostController {

    private static String UPLOADED_FOLDER = "src/main/resources/static/blogCoverImages/";

    private BlogPostService blogPostService;
    private HashTagService hashTagService;
    private CategoryService catService;

    @Autowired
    UserDao users;

    @Autowired
    public CreateBlogPostController(BlogPostService blogPostService, HashTagService hashTagService, CategoryService catService) {
        this.blogPostService = blogPostService;
        this.hashTagService = hashTagService;
        this.catService = catService;
    }

    @GetMapping("create")
    public String loadPage(Model model) {
        List<BlogPost> unPublishedblogPostList = blogPostService.readAllBlogPostsByIsPublished(false);
        model.addAttribute("amountToApprove", unPublishedblogPostList.size());

        List<Category> categoryList = catService.readAllCategorys();

        model.addAttribute("categoryList", categoryList);

        return "create";
    }

    @PostMapping("addBlogPost")
    public String addBlogPost(@Valid BlogPost blogPost, BindingResult result, Model model, HttpServletRequest request,
            @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

        String[] categoryIds = request.getParameterValues("categories");
        List<Category> categories = new ArrayList<>();

        if (categoryIds != null) {
            for (String categoryId : categoryIds) {
                try {
                    categories.add(catService.readCategoryById(Integer.parseInt(categoryId)));
                } catch (EmptyResultDataAccessException e) {

                }
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
            try {
                hashTags.add(hashTagService.readHashTagByName(newTag.getHashTagName()));
                hashTagIds.add(hashTagService.readHashTagByName(newTag.getHashTagName()).getHashTagId() + "");
            } catch (EmptyResultDataAccessException e) {

            }
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
        BlogPost newPost = blogPostService.createBlogPost(blogPost, hashTagIds, categoryIds);

        if (!file.isEmpty()) {

            try {

                // Get the file and save it somewhere
                byte[] bytes = file.getBytes();
                Path path = Paths.get(UPLOADED_FOLDER + "featuredImage" + newPost.getBlogId() + ".jpg");
                Files.write(path, bytes);

                String pathString = path.toString();
                String fixedPath = pathString.substring(25);

                blogPostService.addImage(fixedPath, newPost.getBlogId());

                redirectAttributes.addFlashAttribute("message",
                        "You successfully uploaded '" + file.getOriginalFilename() + "'");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //tell spring to redirect user to mapping locations
        return "redirect:/create";
    }

}
