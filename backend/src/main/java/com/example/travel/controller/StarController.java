package com.example.travel.controller;

import com.example.travel.bean.ArticleBean;
import com.example.travel.service.ArticleService;
import com.example.travel.service.StarService;
import com.example.travel.service.UserService;
import com.example.travel.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author 杨乐乐
 * @time 2023-04-13 10:04
 */
@RestController
@RequestMapping("star")
public class StarController {

    /*TODO: 获取所有文章点赞总和
            添加文章点赞
            取消文章点赞
     */
    @Autowired
    StarService starService;

    @Autowired
    ArticleService articleService;

    /**
     *
     * @param id
     * @return Result
     */
    @GetMapping("/allCounts/{id}")
    public Result allCounts(@PathVariable("id") Integer id){
        Integer res = 0;
        List<ArticleBean> Articles = articleService.getArticleListByAuthor(id);
        for (ArticleBean Article:Articles) {
            res+=Article.getStarCount();
        }
        return Result.success(res);
    }
}
