package com.example.travel.service;

import com.example.travel.bean.TagsForArticleBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.travel.mapper.TagMapper;

import java.util.List;

/**
 * @author 杨乐乐
 * @time 2023-04-07 20:51
 */
@Service
public class TagService {
    @Autowired
    private TagMapper tagMapper;
    //返回全部文章标签
    public List<TagsForArticleBean> getArticleTags(){return tagMapper.getArticleTags();}

    //根据标签id找到相应TagsForArticleBean
    public TagsForArticleBean getTagsForArticleBeanByTagId(Integer tagId){return tagMapper.getTagsForArticleBeanByTagId(tagId);}

    //建立文章标签联系
    public int AddArticleTags(Integer articleId,Integer tagId){return tagMapper.AddArticleTags(articleId, tagId);}
    //返回某篇文章的全部标签id
    public List<Integer>getArticleTagsByArticleId(Integer id){return tagMapper.getArticleTagsByArticleId(id);}

    public boolean haveTag(int articleId,int tagId){
        return tagMapper.haveTag(articleId,tagId) != 0;
    }

    public Integer checkTag(String articleTag){
        return tagMapper.checkTag(articleTag);
    }
    public Integer addtag(String tag){
        return tagMapper.addtag(tag);
    }

    public Integer updateTag(Integer tagArticleNid, String articleTag) {
        return tagMapper.updateTag(tagArticleNid,articleTag);
    }

    public Integer deleteTag(Integer tagArticleNid) {
        return tagMapper.deleteTag(tagArticleNid);
    }
    public Integer deleteTagArticle(Integer tagId){
        return tagMapper.deleteTagArticle(tagId);
    }
}
