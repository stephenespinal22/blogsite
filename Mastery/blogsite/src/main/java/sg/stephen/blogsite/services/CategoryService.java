/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.stephen.blogsite.services;

import java.util.List;
import sg.stephen.blogsite.dtos.Category;

/**
 *
 * @author stephenespinal
 */
public interface CategoryService {

    Category createCategory(Category category);

    List<Category> readAllCategorys();

    Category readCategoryById(int id);

    void updateCategory(Category category);

    void deleteCategory(int id);
}
