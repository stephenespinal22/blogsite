/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.stephen.blogsite.daos;

import java.util.List;
import sg.stephen.blogsite.dtos.HashTag;

/**
 *
 * @author stephenespinal
 */
public interface HashTagDao {
    
    HashTag createHashTag(HashTag hashtag);

    List<HashTag> readAllHashTags();

    HashTag readHashTagById(int id);

    void updateHashTag(HashTag hashtag);

    void deleteHashTag(int id);

    public List<HashTag> getAllHashTagsByBlogId(int blogId);

    public HashTag readHashTagByName(String hashTagName);
}
