package com.example.travel.service;

import com.example.travel.bean.ArticleBean;
import com.example.travel.mapper.ArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {
    @Autowired
    ArticleMapper articleMapper;

    public List<ArticleBean> AdminGetArticleList(){
        return articleMapper.AdminGetArticleList();
    }
    public List<ArticleBean> getArticleListByAuthorName(String authorName){
        return articleMapper.getArticleListByAuthorName(authorName);
    }
    public List<ArticleBean> getArticleListByTitle(String articleTitle){
        return articleMapper.getArticleListByTitle(articleTitle);
    }
    public List<ArticleBean> getArticleListByStatus(Integer articleStatus){
        return  articleMapper.getArticleListByStatus(articleStatus);
    }
    public List<ArticleBean> getArticleListByRecommend(Integer articleRecommend){
        return articleMapper.getArticleListByRecommend(articleRecommend);
    }
    public List<ArticleBean> getArticleList(){
        return articleMapper.getArticleList();
    }
    public ArticleBean getArticleByNid(Integer articleNid){
        return articleMapper.getArticleByNid(articleNid);
    }

    public List<ArticleBean> getArticleListByAuthor(Integer id){
        return articleMapper.getArticleListByAuthor(id);
    }
    public int addArticle(ArticleBean articleBean){
        return articleMapper.addArticle(articleBean);
    }
    public int updateArticle(ArticleBean articleBean){
        return articleMapper.updateArticle(articleBean);
    }
    public int deleteArticleByNid(int articleNid){
        return articleMapper.deleteArticleByNid(articleNid);
    }

    public Integer ArticleCount(String name) {
        return articleMapper.ArticleCount(name);
    }

    public List<ArticleBean> RecommendArticles() {
        return articleMapper.RecommendArticles();
    }

    public List<ArticleBean> RandomArticles() {
        return articleMapper.RandomArticles();
    }

    public List<ArticleBean> getArticleBySiteId(Integer siteId) {
        return articleMapper.getArticleBySiteId(siteId);
    }

    public ArticleBean getArticleByTitle(String articleTitle) {
        return articleMapper.getArticleByTitle(articleTitle);
    }

    public Integer getArticleNum() {
        return  articleMapper.getArticleNum().get(0);
    }
}
