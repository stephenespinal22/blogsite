/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.stephen.blogsite.daos;

import java.util.List;
import sg.stephen.blogsite.dtos.BlogPost;

/**
 *
 * @author stephenespinal
 */
public interface BlogPostDao {

    BlogPost createBlogPost(BlogPost blogPost);

    List<BlogPost> readAllBlogPosts();

    BlogPost readBlogPostById(int id);

    void updateBlogPost(BlogPost blogPost);

    void updateBlogPostIsPublishedById(BlogPost blogPost);

    void deleteBlogPost(int id);

    List<BlogPost> readAllBlogPostsByUserName(String userName);

    List<BlogPost> readAllBlogPostsByIsPublished(Boolean bool);

    List<BlogPost> readAllBlogPostsByHashTagId(int hashTagId);
    
    List<BlogPost> readAllPublishedBlogPostsByHashTagId(String hashTagName, Boolean isPublished);

    List<BlogPost> readAllBlogPostsByCategoryId(int hashTagId);

    void addImage(String imagePath, int id);
}
