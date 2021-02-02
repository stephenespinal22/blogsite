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
import sg.stephen.blogsite.dtos.HashTag;

@Repository
public class HashTagDaoImpl implements HashTagDao {

    private final JdbcTemplate jdbcTemplate;

    private final String insertHashTag = "INSERT IGNORE INTO HashTags (hashTagName) VALUES (?)"; //create
    private final String selectAllHashTags = "SELECT Hashtags.hashTagId, hashTagName FROM HashTags"; //read all
    private final String selectHashTagById = selectAllHashTags + " WHERE hashTagId = ?"; //readbyId
    private final String selectHashTagByName = selectAllHashTags + " WHERE hashTagName = ?"; //readbyName
    private final String getAllHashTagsByBlogId = "SELECT HashTags.hashTagId, hashTagName FROM HashTags JOIN BlogPostsHashTags as bpht ON HashTags.hashTagId = bpht.hashTagId WHERE bpht.blogId = ?";
    private final String updateHashTagById = "UPDATE IGNORE HashTags SET hashTagName = ? WHERE hashTagId = ?"; //update
    private final String deleteHashTagById = "DELETE FROM HashTags WHERE hashTagId = ?"; //delete

    @Autowired
    public HashTagDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public HashTag createHashTag(HashTag hashtag) {
        jdbcTemplate.update(insertHashTag, hashtag.getHashTagName());
        hashtag.setHashTagId(jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class));
        return hashtag;
    }

    @Override
    public List<HashTag> readAllHashTags() {
        return jdbcTemplate.query(selectAllHashTags, new HashTagJDBCMapper());
    }

    @Override
    public HashTag readHashTagById(int id) {
        return jdbcTemplate.queryForObject(selectHashTagById, new HashTagJDBCMapper(), id);
    }

    @Override
    public HashTag readHashTagByName(String hashTagName) {
        return jdbcTemplate.queryForObject(selectHashTagByName, new HashTagJDBCMapper(), hashTagName);
    }

    @Override
    public List<HashTag> getAllHashTagsByBlogId(int blogId) {
        return jdbcTemplate.query(getAllHashTagsByBlogId, new HashTagJDBCMapper(), blogId);
    }

    @Override
    public void updateHashTag(HashTag hashtag) {
        jdbcTemplate.update(updateHashTagById, hashtag.getHashTagName(), hashtag.getHashTagId());
    }

    @Override
    public void deleteHashTag(int id) {
        jdbcTemplate.update(deleteHashTagById, id);
    }

    private class HashTagJDBCMapper implements org.springframework.jdbc.core.RowMapper<HashTag> {

        @Override
        public HashTag mapRow(ResultSet rs, int i) throws SQLException {
            HashTag hashTag = new HashTag();

            hashTag.setHashTagId(rs.getInt("hashTagId"));
            hashTag.setHashTagName(rs.getString("hashTagName"));

            return hashTag;
        }

    }
}
