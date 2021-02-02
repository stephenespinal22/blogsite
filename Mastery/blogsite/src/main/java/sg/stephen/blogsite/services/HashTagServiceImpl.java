/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sg.stephen.blogsite.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sg.stephen.blogsite.daos.BlogPostsHashTagsBridgeDao;
import sg.stephen.blogsite.daos.HashTagDao;
import sg.stephen.blogsite.dtos.HashTag;

@Service
public class HashTagServiceImpl implements HashTagService {

    private HashTagDao hashTagDao;
    private BlogPostsHashTagsBridgeDao blogpostHashTagsBridgeDao;

    @Autowired
    public HashTagServiceImpl(HashTagDao hashTagDao, BlogPostsHashTagsBridgeDao blogpostHashTagsBridgeDao) {
        this.hashTagDao = hashTagDao;
        this.blogpostHashTagsBridgeDao = blogpostHashTagsBridgeDao;
    }

    @Override
    public HashTag createHashTag(HashTag hashTag) {
        return hashTagDao.createHashTag(hashTag);
    }

    @Override
    public List<HashTag> readAllHashTags() {
        return hashTagDao.readAllHashTags();
    }

    @Override
    public HashTag readHashTagById(int id) {
        return hashTagDao.readHashTagById(id);
    }

    @Override
    public HashTag readHashTagByName(String hashTagName) {
        return hashTagDao.readHashTagByName(hashTagName);
    }

    @Override
    public void updateHashTag(HashTag hashTag) {
        hashTagDao.updateHashTag(hashTag);
    }

    @Override
    public void deleteHashTag(int id) {
        blogpostHashTagsBridgeDao.deleteRelationshipByHashTagId(id);
        hashTagDao.deleteHashTag(id);
    }

}
