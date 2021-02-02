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
import sg.stephen.blogsite.dtos.Comment;
import sg.stephen.blogsite.dtos.User;

@Repository
public class CommentDaoImpl implements CommentDao {

    private final JdbcTemplate jdbcTemplate;

    private final String insertComment = "INSERT IGNORE INTO Comments (commentContent, blogId, userName) VALUES (?,?,?)"; //create
    private final String selectAllComments = "SELECT Comments.commentId, commentContent, Comments.blogId, userName FROM Comments "
            + "JOIN BlogPosts as bp ON bp.blogId = Comments.blogId "; //read all
    private final String selectAllCommentsByBlogId = selectAllComments + " WHERE bp.blogId = ?"; //readbyId
    private final String selectAllCommentsByUserName = "SELECT Comments.commentId, commentContent, Comments.blogId, userName FROM Comments "
            + "JOIN BlogPosts as bp ON bp.blogId = Comments.blogId WHERE userName = ?"; //read all
    private final String deleteCommentById = "DELETE FROM Comments WHERE commentId = ?"; //delete
    private final String deleteCommentByBlogPostId = "DELETE FROM Comments WHERE blogId = ?"; //delete

    @Autowired
    public CommentDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Comment createComment(Comment comment) {
        jdbcTemplate.update(insertComment, comment.getCommentContent(), comment.getBlogPost().getBlogId(), comment.getUserName());
        comment.setCommentId(jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class));
        return comment;
    }

    @Override
    public List<Comment> readAllComments() {
        return jdbcTemplate.query(selectAllComments, new CommentJDBCMapper());
    }

    @Override
    public List<Comment> readAllCommentsByUserName(String userName) {
        return jdbcTemplate.query(selectAllCommentsByUserName, new CommentJDBCMapper(), userName);
    }

    @Override
    public List<Comment> readCommenstByBlogId(int id) {
        return jdbcTemplate.query(selectAllCommentsByBlogId, new CommentJDBCMapper(), id);
    }

    @Override
    public void deleteComment(int id) {
        jdbcTemplate.update(deleteCommentById, id);
    }

    @Override
    public void deleteCommentByBlogId(int id) {
        jdbcTemplate.update(deleteCommentByBlogPostId, id);
    }

    private class CommentJDBCMapper implements org.springframework.jdbc.core.RowMapper<Comment> {

        @Override
        public Comment mapRow(ResultSet rs, int i) throws SQLException {
            Comment comment = new Comment();

            comment.setCommentId(rs.getInt("commentId"));
            comment.setCommentContent(rs.getString("commentContent"));
            comment.setUserName(rs.getString("userName"));

            BlogPost post = new BlogPost();
            post.setBlogId(rs.getInt("blogId"));

            comment.setBlogPost(post);

            return comment;
        }

    }
}
