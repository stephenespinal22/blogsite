/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.stephen.blogsite.services;

import java.util.List;
import sg.stephen.blogsite.dtos.HashTag;

/**
 *
 * @author stephenespinal
 */
public interface HashTagService {

    HashTag createHashTag(HashTag hashTag);

    List<HashTag> readAllHashTags();

    HashTag readHashTagById(int id);

    void updateHashTag(HashTag hashTag);

    void deleteHashTag(int id);

    public HashTag readHashTagByName(String hashTagName);
}
