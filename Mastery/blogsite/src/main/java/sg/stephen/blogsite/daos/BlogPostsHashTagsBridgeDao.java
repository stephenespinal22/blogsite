/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.stephen.blogsite.daos;

/**
 *
 * @author stephenespinal
 */
public interface BlogPostsHashTagsBridgeDao {

    void createRelationship(int blogPostId, int hashTagId);

    void deleteRelationshipByBlogPostId(int blogPostId);

    void deleteRelationshipByHashTagId(int hashTagId);
}
