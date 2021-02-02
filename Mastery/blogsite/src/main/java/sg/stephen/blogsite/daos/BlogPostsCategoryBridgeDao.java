/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.stephen.blogsite.daos;

import java.util.List;
import sg.stephen.blogsite.dtos.Category;

/**
 *
 * @author stephenespinal
 */
public interface BlogPostsCategoryBridgeDao {

    void createRelationShip(int blogPostId, int categoryId);

    void deleteRelationshipByBlogPostId(int blogPostId);

    void deleteRelationshipByCategoryId(int categoryId);

}
