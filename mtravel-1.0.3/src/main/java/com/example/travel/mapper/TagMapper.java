package com.example.travel.mapper;

import com.example.travel.bean.TagsForArticleBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author 杨乐乐
 * @time 2023-04-07 21:07
 */
@Mapper
public interface TagMapper {
    //获取所有文章标签
    List<TagsForArticleBean> getArticleTags();

    //向ArticleTags表中建立Article和Tag之间的联系
    int AddArticleTags(Integer articleId,Integer tagId);

    //返回某篇文章的全部id
    List<Integer>getArticleTagsByArticleId(Integer articleId);

    //找到指定文章tag
    TagsForArticleBean getTagsForArticleBeanByTagId(Integer tagArticleNid);

    int haveTag(int articleId,int tagId);

    Integer addtag(String articleTag);

    Integer checkTag(String articleTag);

    Integer updateTag(Integer tagArticleNid, String articleTag);

    Integer deleteTag(Integer tagArticleNid);

    Integer deleteTagArticle(Integer tagId);
}
