package com.example.travel.mapper;

import com.example.travel.bean.ArticleBean;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleMapper {
    List<ArticleBean> AdminGetArticleList();
    List<ArticleBean> getArticleListByAuthorName(String authorName);
    List<ArticleBean> getArticleListByTitle(String articleTitle);

    ArticleBean getArticleByTitle(String articleTitle);
    List<ArticleBean> getArticleListByStatus(Integer articleStatus);
    List<ArticleBean> getArticleListByRecommend(Integer articleRecommend);
    List<ArticleBean> getArticleList();
    ArticleBean getArticleByNid(Integer articleNid);
    List<ArticleBean> getArticleListByAuthor(Integer userNid);
    int addArticle(ArticleBean articleBean);
    int updateArticle(ArticleBean articleBean);
    int deleteArticleByNid(int articleNid);

    Integer ArticleCount(String articleAuthor);

    List<ArticleBean> RecommendArticles();

    List<ArticleBean> RandomArticles();

    List<ArticleBean> getArticleBySiteId(Integer siteId);

    List<Integer> getArticleNum();
}
