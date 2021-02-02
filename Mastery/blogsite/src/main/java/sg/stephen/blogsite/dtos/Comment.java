/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.stephen.blogsite.dtos;

import java.util.Objects;

/**
 *
 * @author stephenespinal
 */
public class Comment {

    private int commentId;
    private String commentContent;
    private String userName;
    private String userRole;
    private BlogPost blogPost;

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public BlogPost getBlogPost() {
        return blogPost;
    }

    public void setBlogPost(BlogPost blogPost) {
        this.blogPost = blogPost;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + this.commentId;
        hash = 47 * hash + Objects.hashCode(this.commentContent);
        hash = 47 * hash + Objects.hashCode(this.userName);
        hash = 47 * hash + Objects.hashCode(this.userRole);
        hash = 47 * hash + Objects.hashCode(this.blogPost);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Comment other = (Comment) obj;
        if (this.commentId != other.commentId) {
            return false;
        }
        if (!Objects.equals(this.commentContent, other.commentContent)) {
            return false;
        }
        if (!Objects.equals(this.userName, other.userName)) {
            return false;
        }
        if (!Objects.equals(this.userRole, other.userRole)) {
            return false;
        }
        if (!Objects.equals(this.blogPost, other.blogPost)) {
            return false;
        }
        return true;
    }

    
}
