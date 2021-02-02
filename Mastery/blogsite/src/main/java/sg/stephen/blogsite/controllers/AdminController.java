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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sg.stephen.blogsite.daos.RoleDao;
import sg.stephen.blogsite.daos.UserDao;
import sg.stephen.blogsite.dtos.BlogPost;
import sg.stephen.blogsite.dtos.Role;
import sg.stephen.blogsite.dtos.User;
import sg.stephen.blogsite.services.BlogPostService;
import sg.stephen.blogsite.services.DuplicateIdException;

@Controller
public class AdminController {

    private static String UPLOADED_FOLDER = "src/main/resources/static/profileImages/";

    Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
    Set<ConstraintViolation<User>> errors = new HashSet<>();

    @Autowired
    UserDao users;

    @Autowired
    RoleDao roles;

    @Autowired
    PasswordEncoder encoder;

    private BlogPostService blogPostService;

    @Autowired
    public AdminController(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }

    @GetMapping("/admin")
    public String displayAdminPage(Model model) {
        List<BlogPost> unPublishedblogPostList = blogPostService.readAllBlogPostsByIsPublished(false);
        model.addAttribute("amountToApprove", unPublishedblogPostList.size());
        model.addAttribute("users", users.getAllUsers());
        return "admin";
    }

    @PostMapping("/addUser")
    public String addUser(Model model, String username, String password) {
        try {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setEnabled(true);

            Set<Role> userRoles = new HashSet<>();
            userRoles.add(roles.getRoleByRole("ROLE_CONTRIBUTOR"));
            user.setRoles(userRoles);
            errors = validate.validate(user);

            if (!errors.isEmpty()) {
                model.addAttribute("errors", errors);
                model.addAttribute("users", users.getAllUsers());
                return "admin";

            } else {
                user.setPassword(encoder.encode(password));
                users.createUser(user);
            }

            return "redirect:/admin";

        } catch (DuplicateIdException e) {
            model.addAttribute("errors", e);
            model.addAttribute("users", users.getAllUsers());
            return "admin";
        }
    }

    @PostMapping("/deleteUser")
    @Transactional
    public String deleteUser(Integer id) {

        try {
            Path path = Paths.get(UPLOADED_FOLDER + "profile" + users.getUserById(id).getUsername() + ".jpg");
            Files.deleteIfExists(path);
        } catch (IOException e) {

        }
        
        if (id != 1) {
            users.deleteUser(id);
        }
        return "redirect:/admin";
    }

    @GetMapping("/editUser")
    public String editUserDisplay(Model model, Integer id, Integer error) {
        User user = users.getUserById(id);
        List roleList = roles.getAllRoles();

        model.addAttribute("user", user);
        model.addAttribute("roles", roleList);

        if (error != null) {
            if (error == 1) {
                model.addAttribute("error", "Passwords did not match, password was not updated.");
            }
        }

        return "editUser";
    }

    @PostMapping(value = "/editUser")
    public String editUserAction(String[] roleIdList, Boolean enabled, Integer id) {
        User user = users.getUserById(id);
        if (enabled != null) {
            user.setEnabled(enabled);
        } else {
            user.setEnabled(false);
        }

        Set<Role> roleList = new HashSet<>();
        for (String roleId : roleIdList) {
            Role role = roles.getRoleById(Integer.parseInt(roleId));
            roleList.add(role);
        }
        user.setRoles(roleList);
        users.updateUser(user);

        return "redirect:/admin";
    }

    @PostMapping("editPassword")
    public String editPassword(Model model, Integer id, String password, String confirmPassword) {
        User user = users.getUserById(id);

        User isUserValid = new User();
        isUserValid.setPassword(password);

        errors = validate.validate(isUserValid);

        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("user", users.getUserById(id));
            model.addAttribute("roles", roles.getAllRoles());

            return "editUser";

        }

        if (password.equals(confirmPassword)) {
            user.setPassword(encoder.encode(password));
            users.updateUser(user);
            return "redirect:/admin";
        } else {
            return "redirect:/editUser?id=" + id + "&error=1";
        }
    }

    @PostMapping("upload") // //new annotation since 4.3
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes, HttpServletRequest request) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:admin";
        }

        try {

            User user = users.getUserById(Integer.parseInt(request.getParameter("userId")));

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + "profile" + user.getUsername() + ".jpg");
            Files.write(path, bytes);

            String pathString = path.toString();
            String fixedPath = pathString.substring(25);
            System.out.println(fixedPath);

            //service.addImagePath(fixedPath, superId);
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

        return "redirect:/admin";
    }

    //contributer will add or edit blog 
}
