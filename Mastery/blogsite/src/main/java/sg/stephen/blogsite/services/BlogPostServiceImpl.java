/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.stephen.blogsite.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.stephen.blogsite.daos.BlogPostDao;
import sg.stephen.blogsite.daos.BlogPostsCategoryBridgeDao;
import sg.stephen.blogsite.daos.BlogPostsHashTagsBridgeDao;
import sg.stephen.blogsite.daos.CategoryDao;
import sg.stephen.blogsite.daos.CommentDao;
import sg.stephen.blogsite.daos.HashTagDao;
import sg.stephen.blogsite.dtos.BlogPost;
import sg.stephen.blogsite.dtos.Category;
import sg.stephen.blogsite.dtos.HashTag;

@Service
public class BlogPostServiceImpl implements BlogPostService {

    private BlogPostDao blogPostDao;
    private BlogPostsHashTagsBridgeDao blogHashBridgeDao;
    private BlogPostsCategoryBridgeDao blogCatBridgeDao;
    private HashTagDao hashTagDao;
    private CategoryDao categoryDao;
    private CommentDao commentDao;

    @Autowired
    public BlogPostServiceImpl(BlogPostDao blogPostDao, BlogPostsHashTagsBridgeDao blogHashBridgeDao,
            BlogPostsCategoryBridgeDao blogCatBridgeDao, HashTagDao hashTagDao,
            CategoryDao categoryDao, CommentDao commentDao) {
        this.blogPostDao = blogPostDao;
        this.blogHashBridgeDao = blogHashBridgeDao;
        this.blogCatBridgeDao = blogCatBridgeDao;
        this.hashTagDao = hashTagDao;
        this.categoryDao = categoryDao;
        this.commentDao = commentDao;
    }

    @Override
    @Transactional
    public BlogPost createBlogPost(BlogPost blogPost, List<String> hashTagIds, String[] categoryIds) {
        BlogPost createdPost = blogPostDao.createBlogPost(blogPost);
        if (hashTagIds != null) {

            for (String hashTagId : hashTagIds) {
                try {
                    if (hashTagDao.readHashTagById(Integer.parseInt(hashTagId)) != null) {
                        blogHashBridgeDao.createRelationship(blogPost.getBlogId(), Integer.parseInt(hashTagId));
                    }
                } catch (EmptyResultDataAccessException e) {

                }
            }
        }

        if (categoryIds != null) {

            for (String categoryId : categoryIds) {
                //check for category exists
                try {
                    if (categoryDao.readCategoryById(Integer.parseInt(categoryId)) != null) {
                        blogCatBridgeDao.createRelationShip(blogPost.getBlogId(), Integer.parseInt(categoryId));
                    }
                } catch (EmptyResultDataAccessException e) {

                }

            }
        }

        return createdPost;
    }

    @Override
    public List<BlogPost> readAllBlogPosts() {
        List<BlogPost> blogPostList = blogPostDao.readAllBlogPosts();

        List<HashTag> hashTagsList = new ArrayList<HashTag>();

        List<Category> categoryList = new ArrayList<Category>();

        for (BlogPost blogPost : blogPostList) {
            hashTagsList = hashTagDao.getAllHashTagsByBlogId(blogPost.getBlogId());
            categoryList = categoryDao.getAllCategoriesByBlogId(blogPost.getBlogId());
            blogPost.setHashTags(hashTagsList);
            blogPost.setCategories(categoryList);
        }

        return blogPostList;
    }

    @Override
    public BlogPost readBlogPostById(int id
    ) {
        return blogPostDao.readBlogPostById(id);
    }

    @Override
    public List<BlogPost> readAllBlogPostsByUserName(String userName
    ) {

        List<BlogPost> blogPostListByUserName = blogPostDao.readAllBlogPostsByUserName(userName);

        List<HashTag> hashTagsList = new ArrayList<HashTag>();

        List<Category> categoryList = new ArrayList<Category>();

        for (BlogPost blogPost : blogPostListByUserName) {
            hashTagsList = hashTagDao.getAllHashTagsByBlogId(blogPost.getBlogId());
            categoryList = categoryDao.getAllCategoriesByBlogId(blogPost.getBlogId());
            blogPost.setHashTags(hashTagsList);
            blogPost.setCategories(categoryList);
        }

        return blogPostListByUserName;
    }

    @Override
    public List<BlogPost> readAllBlogPostsByIsPublished(Boolean bool
    ) {
        List<BlogPost> blogPostListByPublishStatus = blogPostDao.readAllBlogPostsByIsPublished(bool);

        List<HashTag> hashTagsList = new ArrayList<HashTag>();

        List<Category> categoryList = new ArrayList<Category>();

        for (BlogPost blogPost : blogPostListByPublishStatus) {
            hashTagsList = hashTagDao.getAllHashTagsByBlogId(blogPost.getBlogId());
            categoryList = categoryDao.getAllCategoriesByBlogId(blogPost.getBlogId());
            blogPost.setHashTags(hashTagsList);
            blogPost.setCategories(categoryList);
        }

        return blogPostListByPublishStatus;
    }

    @Override
    public List<BlogPost> readAllPublishedBlogPostsByHashTagId(String hashTagName, Boolean isPublished
    ) {
        List<BlogPost> blogPostListByPublishStatus = blogPostDao.readAllPublishedBlogPostsByHashTagId(hashTagName, isPublished);

        List<HashTag> hashTagsList = new ArrayList<HashTag>();

        List<Category> categoryList = new ArrayList<Category>();

        for (BlogPost blogPost : blogPostListByPublishStatus) {
            hashTagsList = hashTagDao.getAllHashTagsByBlogId(blogPost.getBlogId());
            categoryList = categoryDao.getAllCategoriesByBlogId(blogPost.getBlogId());
            blogPost.setHashTags(hashTagsList);
            blogPost.setCategories(categoryList);
        }

        return blogPostListByPublishStatus;
    }

    @Override
    @Transactional
    public void updateBlogPost(BlogPost blogPost, List<String> hashTagIds,
            String[] categoryIds
    ) {
        blogHashBridgeDao.deleteRelationshipByBlogPostId(blogPost.getBlogId());
        blogCatBridgeDao.deleteRelationshipByBlogPostId(blogPost.getBlogId());
        if (hashTagIds != null) {

            for (String hashTagId : hashTagIds) {
                blogHashBridgeDao.createRelationship(blogPost.getBlogId(), Integer.parseInt(hashTagId));
            }
        }

        if (categoryIds != null) {

            for (String categoryId : categoryIds) {
                blogCatBridgeDao.createRelationShip(blogPost.getBlogId(), Integer.parseInt(categoryId));
            }
        }
        blogPostDao.updateBlogPost(blogPost);
    }

    @Override
    public void updateBlogPostIsPublishedById(BlogPost blogPost
    ) {
        blogPost.setIsPublished(true);
        blogPostDao.updateBlogPostIsPublishedById(blogPost);
    }

    @Override
    @Transactional
    public void deleteBlogPost(int id
    ) {
        blogHashBridgeDao.deleteRelationshipByBlogPostId(id);
        blogCatBridgeDao.deleteRelationshipByBlogPostId(id);
        commentDao.deleteCommentByBlogId(id);
        blogPostDao.deleteBlogPost(id);
    }

    @Override
    public void addImage(String imagePath, int id
    ) {
        blogPostDao.addImage(imagePath, id);
    }

}
