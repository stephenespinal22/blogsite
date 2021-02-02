/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.stephen.blogsite.dtos;

import java.util.List;
import java.util.Objects;

/**
 *
 * @author stephenespinal
 */
public class BlogPost {
    
    private int blogId;
    private String blogTitle;
    private String blogPreview;
    private String blogContent;
    private boolean isPublished;
    private String postDate;
    private String featuredImagePath;
    private User user;
    
    private List<HashTag> hashTags;
    private List<Category> categories;

    public int getBlogId() {
        return blogId;
    }

    public void setBlogId(int blogId) {
        this.blogId = blogId;
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
    }

    public String getBlogPreview() {
        return blogPreview;
    }

    public void setBlogPreview(String blogPreview) {
        this.blogPreview = blogPreview;
    }

    public String getBlogContent() {
        return blogContent;
    }

    public void setBlogContent(String blogContent) {
        this.blogContent = blogContent;
    }

    public boolean isIsPublished() {
        return isPublished;
    }

    public void setIsPublished(boolean isPublished) {
        this.isPublished = isPublished;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getFeaturedImagePath() {
        return featuredImagePath;
    }

    public void setFeaturedImagePath(String featuredImagePath) {
        this.featuredImagePath = featuredImagePath;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<HashTag> getHashTags() {
        return hashTags;
    }

    public void setHashTags(List<HashTag> hashTags) {
        this.hashTags = hashTags;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + this.blogId;
        hash = 29 * hash + Objects.hashCode(this.blogTitle);
        hash = 29 * hash + Objects.hashCode(this.blogPreview);
        hash = 29 * hash + Objects.hashCode(this.blogContent);
        hash = 29 * hash + (this.isPublished ? 1 : 0);
        hash = 29 * hash + Objects.hashCode(this.postDate);
        hash = 29 * hash + Objects.hashCode(this.featuredImagePath);
        hash = 29 * hash + Objects.hashCode(this.user);
        hash = 29 * hash + Objects.hashCode(this.hashTags);
        hash = 29 * hash + Objects.hashCode(this.categories);
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
        final BlogPost other = (BlogPost) obj;
        if (this.blogId != other.blogId) {
            return false;
        }
        if (this.isPublished != other.isPublished) {
            return false;
        }
        if (!Objects.equals(this.blogTitle, other.blogTitle)) {
            return false;
        }
        if (!Objects.equals(this.blogPreview, other.blogPreview)) {
            return false;
        }
        if (!Objects.equals(this.blogContent, other.blogContent)) {
            return false;
        }
        if (!Objects.equals(this.postDate, other.postDate)) {
            return false;
        }
        if (!Objects.equals(this.featuredImagePath, other.featuredImagePath)) {
            return false;
        }
        if (!Objects.equals(this.user, other.user)) {
            return false;
        }
        if (!Objects.equals(this.hashTags, other.hashTags)) {
            return false;
        }
        if (!Objects.equals(this.categories, other.categories)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "BlogPost{" + "blogId=" + blogId + ", blogTitle=" + blogTitle + ", blogPreview=" + blogPreview + ", blogContent=" + blogContent + ", isPublished=" + isPublished + ", postDate=" + postDate + ", featuredImagePath=" + featuredImagePath + ", user=" + user.toString() + ", hashTags=" + hashTags + ", categories=" + categories + '}';
    }

   
        
}
