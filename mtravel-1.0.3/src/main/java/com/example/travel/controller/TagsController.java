package com.example.travel.controller;

import com.example.travel.bean.TagsForArticleBean;
import com.example.travel.service.TagService;
import com.example.travel.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 杨乐乐
 * @time 2023-04-07 20:49
 */
@RestController
@RequestMapping("/Tags")
public class TagsController {
    @Autowired
    private TagService tagService;

    @GetMapping("/ArticleTags")
    public Result getArticleTags() {
        List<TagsForArticleBean> tags = tagService.getArticleTags();
        if (tags.isEmpty()) {
            return Result.fail(500, "未查询到文章标签");
        }
        return Result.success(tags);
    }

    @GetMapping("/{id}")
    public Result getArticleTagsByArticleId(@PathVariable("id") Integer id) {
        List<Integer> tagsId = tagService.getArticleTagsByArticleId(id);
        List<TagsForArticleBean> tags = new ArrayList<>();
        for (int i = 0; i < tagsId.size(); i++) {
            tags.add(tagService.getTagsForArticleBeanByTagId(tagsId.get(i)));
        }
        return Result.success(tags);
    }

    @PostMapping("/new-tag")
    public Result addtag(@RequestBody TagsForArticleBean tag){
        if(tagService.checkTag(tag.getArticleTag())!=0){
            return Result.fail(590,"标签名已存在");
        }
        tagService.addtag(tag.getArticleTag());
        return Result.success("添加成功");
    }

    @PostMapping("/renew-tag")
    public Result updateTag(@RequestBody TagsForArticleBean tag){
        Integer status = tagService.updateTag(tag.getTagArticleNid(),tag.getArticleTag());
        if(status!=0){
            return Result.success("更新成功");
        }
        else{
            return Result.fail(591,"更新失败");
        }
    }

    @PostMapping("/delete-tag")
    public Result deleteTag(@RequestBody TagsForArticleBean tag){
        Integer status = tagService.deleteTag(tag.getTagArticleNid());
        tagService.deleteTagArticle(tag.getTagArticleNid());
        if(status!=0){
            return Result.success("删除成功");
        }
        else{
            return Result.fail(591,"删除失败");
        }
    }

}