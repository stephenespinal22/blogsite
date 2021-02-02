/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.stephen.blogsite.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sg.stephen.blogsite.daos.BlogPostsCategoryBridgeDao;
import sg.stephen.blogsite.daos.CategoryDao;
import sg.stephen.blogsite.dtos.Category;


@Service
public class CategoryServiceImpl implements CategoryService {
    
    private CategoryDao categoryDao;
    private BlogPostsCategoryBridgeDao blogPostCatDao;
    
    @Autowired
    public CategoryServiceImpl(CategoryDao categoryDao,BlogPostsCategoryBridgeDao blogPostCatDao) {
        this.categoryDao = categoryDao;
        this.blogPostCatDao = blogPostCatDao;
    }
    

    @Override
    public Category createCategory(Category category) {
        return categoryDao.createCategory(category);
    }

    @Override
    public List<Category> readAllCategorys() {
        return categoryDao.readAllCategorys();
    }

    @Override
    public Category readCategoryById(int id) {
        return categoryDao.readCategoryById(id);
    }

    @Override
    public void updateCategory(Category category) {
        categoryDao.updateCategory(category);
    }

    @Override
    public void deleteCategory(int id) {
        blogPostCatDao.deleteRelationshipByCategoryId(id);
        categoryDao.deleteCategory(id);
    }
    
}
