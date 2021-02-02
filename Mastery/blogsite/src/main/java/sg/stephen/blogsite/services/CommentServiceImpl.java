/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.stephen.blogsite.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sg.stephen.blogsite.daos.CommentDao;
import sg.stephen.blogsite.dtos.Comment;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentDao commentDao;

    @Autowired
    public CommentServiceImpl(CommentDao commentDao) {
        this.commentDao = commentDao;
    }

    @Override
    public Comment createComment(Comment comment) {
        return commentDao.createComment(comment);
    }

    @Override
    public List<Comment> readAllComments() {
        return commentDao.readAllComments();
    }

    @Override
    public List<Comment> readCommenstByBlogId(int id) {
        return commentDao.readCommenstByBlogId(id);
    }

    @Override
    public void deleteComment(int id) {
        commentDao.deleteComment(id);
    }

}
