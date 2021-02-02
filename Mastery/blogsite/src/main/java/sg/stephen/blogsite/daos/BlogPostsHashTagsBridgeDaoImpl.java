/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.stephen.blogsite.daos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BlogPostsHashTagsBridgeDaoImpl implements BlogPostsHashTagsBridgeDao {

    private final JdbcTemplate jdbc;

    private final String insertBlogPostsHashTag = "Insert Into BlogPostsHashTags(blogId, hashTagId) values (?,?);";
    private final String deleteByBlogPost = "Delete From BlogPostsHashTags where blogId = ?;";
    private final String deleteHashTag = "Delete From BlogPostsHashTags where hashTagId = ?;";

    @Autowired
    public BlogPostsHashTagsBridgeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbc = jdbcTemplate;
    }

    @Override
    public void createRelationship(int blogPostId, int hashTagId) {
        jdbc.update(insertBlogPostsHashTag, blogPostId, hashTagId);
    }

    @Override
    public void deleteRelationshipByBlogPostId(int blogPostId) {
        jdbc.update(deleteByBlogPost, blogPostId);
    }

    @Override
    public void deleteRelationshipByHashTagId(int hashTagId) {
        jdbc.update(deleteHashTag, hashTagId);
    }

}
