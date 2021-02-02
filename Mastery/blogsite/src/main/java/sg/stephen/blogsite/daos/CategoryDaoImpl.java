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
import sg.stephen.blogsite.dtos.Category;

@Repository
public class CategoryDaoImpl implements CategoryDao {

    private final JdbcTemplate jdbcTemplate;

    private final String insertCategory = "INSERT IGNORE INTO Category (categoryName) VALUES (?)"; //create
    private final String selectAllCategories = "SELECT Category.categoryId, categoryName FROM Category"; //read all
    private final String selectCategoryById = selectAllCategories + " WHERE categoryId = ?"; //readbyId
    private final String getAllCategoriesByBlogId = selectAllCategories + " JOIN BlogPostsCategory as bpc ON Category.categoryId = bpc.categoryId WHERE bpc.blogId = ?";
    private final String updateCategoryById = "UPDATE IGNORE Category SET categoryName = ? WHERE categoryId = ?"; //update
    private final String deleteCategoryById = "DELETE FROM Category WHERE categoryId = ?"; //delete

    @Autowired
    public CategoryDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Category createCategory(Category category) {
        jdbcTemplate.update(insertCategory, category.getCategoryName());
        category.setCategoryId(jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class));
        return category;
    }

    @Override
    public List<Category> readAllCategorys() {
        return jdbcTemplate.query(selectAllCategories, new CategoryJDBCMapper());
    }

    @Override
    public Category readCategoryById(int id) {
        return jdbcTemplate.queryForObject(selectCategoryById, new CategoryJDBCMapper(), id);
    }

    @Override
    public List<Category> getAllCategoriesByBlogId(int blogId) {
        return jdbcTemplate.query(getAllCategoriesByBlogId, new CategoryJDBCMapper(), blogId);
    }

    @Override
    public void updateCategory(Category category) {
        jdbcTemplate.update(updateCategoryById, category.getCategoryName(), category.getCategoryId());
    }

    @Override
    public void deleteCategory(int id) {
        jdbcTemplate.update(deleteCategoryById, id);
    }

    private class CategoryJDBCMapper implements org.springframework.jdbc.core.RowMapper<Category> {

        @Override
        public Category mapRow(ResultSet rs, int i) throws SQLException {
            Category category = new Category();

            category.setCategoryId(rs.getInt("categoryId"));
            category.setCategoryName(rs.getString("categoryName"));

            return category;
        }

    }
}
