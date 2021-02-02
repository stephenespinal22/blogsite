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
public class BlogPostsCategoryBridgeDaoImpl implements BlogPostsCategoryBridgeDao {

    private final JdbcTemplate jdbc;

    private final String insertBlogPostsCategory = "Insert Into BlogPostsCategory(blogId, categoryId) values (?,?);";
    private final String deleteByBlogPost = "Delete From BlogPostsCategory where blogId = ?;";
    private final String deleteHashTag = "Delete From BlogPostsCategory where categoryId = ?;";

    @Autowired
    public BlogPostsCategoryBridgeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbc = jdbcTemplate;
    }

    @Override
    public void createRelationShip(int blogPostId, int categoryId) {
        jdbc.update(insertBlogPostsCategory, blogPostId, categoryId);
    }

    @Override
    public void deleteRelationshipByBlogPostId(int blogPostId) {
        jdbc.update(deleteByBlogPost, blogPostId);
    }

    @Override
    public void deleteRelationshipByCategoryId(int categoryId) {
        jdbc.update(deleteHashTag, categoryId);
    }

}
