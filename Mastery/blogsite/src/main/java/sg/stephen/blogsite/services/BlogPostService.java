/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.stephen.blogsite.services;

import java.util.List;
import sg.stephen.blogsite.dtos.BlogPost;

/**
 *
 * @author stephenespinal
 */
public interface BlogPostService {

    BlogPost createBlogPost(BlogPost blogPost, List<String> hashTagIds, String[] categoryIds);

    List<BlogPost> readAllBlogPosts();

    BlogPost readBlogPostById(int id);

    List<BlogPost> readAllBlogPostsByUserName(String userName);

    List<BlogPost> readAllBlogPostsByIsPublished(Boolean bool);

    List<BlogPost> readAllPublishedBlogPostsByHashTagId(String hashTagName, Boolean isPublished);

    void updateBlogPost(BlogPost blogPost, List<String> hashTagIds, String[] categoryIds);

    void updateBlogPostIsPublishedById(BlogPost blogPost);

    void deleteBlogPost(int id);

    void addImage(String imagePath, int id);
}
