/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.stephen.blogsite.daos;

import java.util.List;
import sg.stephen.blogsite.dtos.Comment;

/**
 *
 * @author stephenespinal
 */
public interface CommentDao {

    Comment createComment(Comment comment);

    List<Comment> readAllComments();

    List<Comment> readCommenstByBlogId(int id);

    void deleteComment(int id);

    List<Comment> readAllCommentsByUserName(String userName);
    
    void deleteCommentByBlogId(int id);
}
