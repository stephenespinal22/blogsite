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
import sg.stephen.blogsite.dtos.Category;
import sg.stephen.blogsite.services.BlogPostService;
import sg.stephen.blogsite.services.CategoryService;

/**
 *
 * @author stephenespinal
 */
@Controller
public class CategoryController {

    private CategoryService categoryService;

    private BlogPostService blogPostService;

    @Autowired
    public CategoryController(CategoryService categoryService, BlogPostService blogPostService) {
        this.categoryService = categoryService;
        this.blogPostService = blogPostService;
    }

    @GetMapping("category")
    public String loadPage(Model model) {
        List<BlogPost> unPublishedblogPostList = blogPostService.readAllBlogPostsByIsPublished(false);
        model.addAttribute("amountToApprove", unPublishedblogPostList.size());
        List<Category> categoryList = categoryService.readAllCategorys();

        model.addAttribute("categoryList", categoryList);

        return "category";
    }

    @PostMapping("addNewCategory")
    public String addNewCategory(@Valid Category category, BindingResult result, Model model) {

        categoryService.createCategory(category);

        //tell spring to redirect user to mapping locations
        return "redirect:/category";
    }

    @PostMapping("editCategory")
    public String editCategory(@Valid Category category, BindingResult result, Model model) {

        categoryService.updateCategory(category);

        //tell spring to redirect user to mapping locations
        return "redirect:/category";
    }

    @PostMapping("deleteCategory")
    public String deleteCategory(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("categoryId"));
        categoryService.deleteCategory(id);

        return "redirect:/category";
    }
}
