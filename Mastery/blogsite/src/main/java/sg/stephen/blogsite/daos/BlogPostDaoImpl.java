/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.stephen.blogsite.daos;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sg.stephen.blogsite.dtos.BlogPost;
import sg.stephen.blogsite.dtos.User;

@Repository
public class BlogPostDaoImpl implements BlogPostDao {

    private final JdbcTemplate jdbcTemplate;

    private final String insertBlogPost = "INSERT INTO BlogPosts (blogTitle,blogPreview,blogContent,postDate,userId) VALUES (?,?,?,?,?)"; //create
    private final String selectAllBlogPosts = "SELECT blogId, blogPreview, blogTitle, blogContent, isPublished, postDate, featuredImagePath, BlogPosts.userId, `username` FROM BlogPosts"
            + " JOIN `user` as Users ON Users.`id` = BlogPosts.userId "; //read all
    private final String selectAllBlogPostsOrdered = "SELECT blogId, blogPreview, blogTitle, blogContent, isPublished, postDate, featuredImagePath, BlogPosts.userId, `username` FROM BlogPosts"
            + " JOIN `user` as Users ON Users.`id` = BlogPosts.userId ORDER BY STR_TO_DATE(postDate,'%m/%d/%Y %h:%i %p') DESC"; //read all
    private final String selectBlogPostById = selectAllBlogPosts + " WHERE blogId = ?"; //readbyId
    private final String selectAllBlogPostsByUserName = selectAllBlogPosts + " WHERE `username` = ? ORDER BY STR_TO_DATE(postDate,'%m/%d/%Y %h:%i %p') DESC"; //readbyUserId

    private final String selectAllBlogPostsByIsPublished = selectAllBlogPosts + " WHERE isPublished = ? ORDER BY STR_TO_DATE(postDate,'%m/%d/%Y %h:%i %p') DESC"; //readbyUserId

    private final String selectAllBlogPostsByHashTagId = selectAllBlogPosts + " JOIN BlogPostsHashTags as bpht ON BlogPosts.blogId = bpht.blogId WHERE bpht.blogId = ?";

    private final String selectAllPublishedBlogPostsByHashTagId = "SELECT DISTINCT BlogPosts.blogId, blogPreview, blogTitle, blogContent, isPublished, postDate, featuredImagePath, BlogPosts.userId, `username` FROM BlogPosts"
            + " JOIN `user` as Users ON Users.`id` = BlogPosts.userId "
            + " LEFT JOIN BlogPostsHashTags as bpht ON BlogPosts.blogId = bpht.blogId "
            + " LEFT JOIN HashTags as ht ON ht.hashTagId = bpht.hashTagId "
            + " LEFT JOIN BlogPostsCategory as bpc ON BlogPosts.blogId = bpc.blogId "
            + " LEFT JOIN Category as cat ON cat.categoryId = bpc.categoryId WHERE ht.hashTagName LIKE ? OR cat.categoryName LIKE ? AND isPublished = ? ";

    private final String selectAllBlogPostsByCategoryId = selectAllBlogPosts + " JOIN BlogPostsCategory as bpc ON BlogPosts.blogId = bpc.blogId WHERE bpc.categoryId = ?";

    private final String updateBlogPostById = "UPDATE BlogPosts SET blogTitle = ?, blogPreview = ?, blogContent = ?, isPublished = ?, postDate = ? WHERE blogId = ?"; //update
    private final String updateBlogPostIsPublishedById = "UPDATE BlogPosts SET isPublished = ? WHERE blogId = ?"; //update
    private final String deleteBlogPostById = "DELETE FROM BlogPosts WHERE blogId = ?"; //delete

    private final String addImagePath = "Update BlogPosts Set featuredImagePath = ? WHERE blogId = ?";

    @Autowired
    public BlogPostDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BlogPost createBlogPost(BlogPost blogPost) {
        jdbcTemplate.update(insertBlogPost, blogPost.getBlogTitle(), blogPost.getBlogPreview(), blogPost.getBlogContent(), blogPost.getPostDate(), blogPost.getUser().getId());
        blogPost.setBlogId(jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class));
        return blogPost;
    }

    @Override
    public List<BlogPost> readAllBlogPosts() {
        return jdbcTemplate.query(selectAllBlogPostsOrdered, new BlogPostJDBCMapper());
    }

    @Override
    public List<BlogPost> readAllBlogPostsByUserName(String userName) {
        return jdbcTemplate.query(selectAllBlogPostsByUserName, new BlogPostJDBCMapper(), userName);
    }

    @Override
    public List<BlogPost> readAllBlogPostsByIsPublished(Boolean bool) {
        return jdbcTemplate.query(selectAllBlogPostsByIsPublished, new BlogPostJDBCMapper(), bool);
    }

    @Override
    public List<BlogPost> readAllBlogPostsByHashTagId(int hashTagId) {
        return jdbcTemplate.query(selectAllBlogPostsByHashTagId, new BlogPostJDBCMapper(), hashTagId);
    }
    
    @Override
    public List<BlogPost> readAllPublishedBlogPostsByHashTagId(String hashTagName, Boolean isPublished) {
        return jdbcTemplate.query(selectAllPublishedBlogPostsByHashTagId, new BlogPostJDBCMapper(), "%" + hashTagName + "%","%" + hashTagName + "%", isPublished);
    }

    @Override

    public List<BlogPost> readAllBlogPostsByCategoryId(int categoryId) {
        return jdbcTemplate.query(selectAllBlogPostsByCategoryId, new BlogPostJDBCMapper(), categoryId);
    }

    @Override
    public BlogPost readBlogPostById(int id) {
        return jdbcTemplate.queryForObject(selectBlogPostById, new BlogPostJDBCMapper(), id);
    }

    @Override
    public void updateBlogPost(BlogPost blogPost) {
        jdbcTemplate.update(updateBlogPostById, blogPost.getBlogTitle(), blogPost.getBlogPreview(), blogPost.getBlogContent(), blogPost.isIsPublished(), blogPost.getPostDate(), blogPost.getBlogId());
    }

    @Override
    public void updateBlogPostIsPublishedById(BlogPost blogPost) {
        jdbcTemplate.update(updateBlogPostIsPublishedById, blogPost.isIsPublished(), blogPost.getBlogId());
    }

    @Override
    public void deleteBlogPost(int id) {
        jdbcTemplate.update(deleteBlogPostById, id);
    }

    @Override
    public void addImage(String imagePath, int id) {
        jdbcTemplate.update(addImagePath, imagePath, id);
    }

    private class BlogPostJDBCMapper implements org.springframework.jdbc.core.RowMapper<BlogPost> {

        @Override
        public BlogPost mapRow(ResultSet rs, int i) throws SQLException {
            BlogPost blogPost = new BlogPost();

            blogPost.setBlogId(Integer.parseInt(rs.getString("blogId")));
            blogPost.setBlogTitle(rs.getString("blogTitle"));
            blogPost.setBlogPreview(rs.getString("blogPreview"));
            blogPost.setBlogContent(rs.getString("blogContent"));
            blogPost.setIsPublished(rs.getBoolean("isPublished"));
            blogPost.setPostDate(rs.getString("postDate"));
            blogPost.setFeaturedImagePath(rs.getString("featuredImagePath"));

            User user = new User();
            user.setId(Integer.parseInt(rs.getString("userId")));
            user.setUsername(rs.getString("username"));

            //using composition
            blogPost.setUser(user);

            return blogPost;
        }

    }
}
