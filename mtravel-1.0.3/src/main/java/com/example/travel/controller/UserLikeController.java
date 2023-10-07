package com.example.travel.controller;

import com.example.travel.bean.*;
import com.example.travel.service.*;
import com.example.travel.vo.Pageparam;
import com.example.travel.vo.Result;
import com.example.travel.vo.manualPageVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.Math.min;

@RestController
@RequestMapping("/like")
public class UserLikeController {
    @Autowired
    UserLikeCommentService userLikeCommentService;
    @Autowired
    UserLikeArticleService userLikeArticleService;
    @Autowired
    UserLikePicAlbumService userLikePicAlbumService;
    @Autowired
    PicAlbumService picAlbumService;
    @Autowired
    CollectService collectService;
    @Autowired
    CommentService commentService;
    @Autowired
    ArticleService articleService;
    @Autowired
    UserService userService;
    @Autowired
    SiteService siteService;

    @PostMapping("/addCommentLike")
    public Result addCommentLike(@RequestBody Map<String,Object> commentLikeData){
        Integer userId = Integer.parseInt(commentLikeData.get("userId").toString());
        Integer commentId = Integer.parseInt(commentLikeData.get("commentId").toString());
        Integer status = userLikeCommentService.addLike(userId,commentId);
        CommentBean commentBean = commentService.getCommentByNid(commentId);
        commentBean.setStarCount(commentBean.getStarCount()+1);
        commentService.updateComment(commentBean);
        if(status>0){
            return Result.success("success");
        }
        return Result.fail(2200,"failed");
    }

    @PostMapping("/delCommentLike")
    public Result delCommentLike(@RequestBody Map<String,Object> commentLikeData){
        Integer userId = Integer.parseInt(commentLikeData.get("userId").toString());
        Integer commentId = Integer.parseInt(commentLikeData.get("commentId").toString());
        Integer status = userLikeCommentService.delLike(userId,commentId);
        CommentBean commentBean = commentService.getCommentByNid(commentId);
        commentBean.setStarCount(commentBean.getStarCount()-1);
        commentService.updateComment(commentBean);
        if(status>0){
            return Result.success("success");
        }
        return Result.fail(2200,"failed");
    }

    @PostMapping("/addArticleLike")
    public Result addArticleLike(@RequestBody Map<String,Object> articleLikeData){
        Integer userId = Integer.parseInt(articleLikeData.get("userId").toString());
        Integer articleId = Integer.parseInt(articleLikeData.get("articleId").toString());
        Integer status = userLikeArticleService.addLike(userId,articleId);
        ArticleBean articleBean = articleService.getArticleByNid(articleId);
        articleBean.setStarCount(articleBean.getStarCount()+1);
        articleService.updateArticle(articleBean);
        if(status>0){
            return Result.success("success");
        }
        return Result.fail(2200,"failed");
    }

    @PostMapping("/delArticleLike")
    public Result delArticleLike(@RequestBody Map<String,Object> articleLikeData){
        Integer userId = Integer.parseInt(articleLikeData.get("userId").toString());
        Integer articleId = Integer.parseInt(articleLikeData.get("articleId").toString());
        Integer status = userLikeArticleService.delLike(userId,articleId);
        ArticleBean articleBean = articleService.getArticleByNid(articleId);
        articleBean.setStarCount(articleBean.getStarCount()-1);
        articleService.updateArticle(articleBean);
        if(status>0){
            return Result.success("success");
        }
        return Result.fail(2200,"failed");
    }

    @PostMapping("/addArticleCollect")
    public Result addArticleCollect(@RequestBody Map<String,Object> articleCollectData){
        Integer userId = Integer.parseInt(articleCollectData.get("userId").toString());
        Integer articleId = Integer.parseInt(articleCollectData.get("articleId").toString());
        Integer status = collectService.addCollect(userId,articleId);
        ArticleBean articleBean = articleService.getArticleByNid(articleId);
        articleBean.setCollectedCount(articleBean.getCollectedCount()+1);
        articleService.updateArticle(articleBean);
        if(status>0){
            return Result.success("success");
        }
        return Result.fail(2200,"failed");
    }

    @PostMapping("/delArticleCollect")
    public Result delArticleCollect(@RequestBody Map<String,Object> articleCollectData){
        Integer userId = Integer.parseInt(articleCollectData.get("userId").toString());
        Integer articleId = Integer.parseInt(articleCollectData.get("articleId").toString());
        Integer status = collectService.delCollect(userId,articleId);
        ArticleBean articleBean = articleService.getArticleByNid(articleId);
        articleBean.setCollectedCount(articleBean.getCollectedCount()-1);
        articleService.updateArticle(articleBean);
        if(status>0){
            return Result.success("success");
        }
        return Result.fail(2200,"failed");
    }

    @PostMapping("/getCollectArticleListByUserId/{id}")
    public Result getCollectArticleListByUserId(@PathVariable("id") Integer id, @RequestBody Pageparam pageparam){
        Integer userId = id;
        List<CollectBean>collectBeanList = collectService.getCollectArticleListByUserId(userId);
        List<ArticleBean>articleBeanList = new ArrayList<>();
        for(int i=0;i<collectBeanList.size();i++){
            articleBeanList.add(articleService.getArticleByNid(collectBeanList.get(i).getArticleId()));
        }
        int pageNum = pageparam.getPagenum();
        int pageSize = pageparam.getPagesize();
        List<ArticleBean>ans = new ArrayList<>();
        int startIdx = (pageNum-1)*pageSize;
        for(int i=startIdx;i<min(startIdx+pageSize,articleBeanList.size());i++){
            ans.add(articleBeanList.get(i));
        }
        manualPageVo manualPageVo = new manualPageVo(ans,articleBeanList.size());
        return Result.success(manualPageVo);
    }
    @PostMapping("/addPicAlbumLike")
    public Result addPicAlbumLike(@RequestBody Map<String,Object> picAlbumLikeData){
        Integer userId = Integer.parseInt(picAlbumLikeData.get("userId").toString());
        Integer picAlbumId = Integer.parseInt(picAlbumLikeData.get("picAlbumId").toString());
        Integer status = userLikePicAlbumService.addLike(userId,picAlbumId);
        PicAlbumBean picAlbumBean = picAlbumService.getPicAlbumByNid(picAlbumId);
        picAlbumBean.setLikeCount(picAlbumBean.getLikeCount()+1);
        picAlbumService.updatePicAlbum(picAlbumBean);
        if(status>0){
            return Result.success("success");
        }
        return Result.fail(2200,"failed");
    }
    @PostMapping("/delPicAlbumLike")
    public Result delPicAlbumLike(@RequestBody Map<String,Object> picAlbumLikeData){
        Integer userId = Integer.parseInt(picAlbumLikeData.get("userId").toString());
        Integer picAlbumId = Integer.parseInt(picAlbumLikeData.get("picAlbumId").toString());
        Integer status = userLikePicAlbumService.delLike(userId,picAlbumId);
        PicAlbumBean picAlbumBean = picAlbumService.getPicAlbumByNid(picAlbumId);
        picAlbumBean.setLikeCount(picAlbumBean.getLikeCount()-1);
        picAlbumService.updatePicAlbum(picAlbumBean);
        if(status>0){
            return Result.success("success");
        }
        return Result.fail(2200,"failed");
    }
    @PostMapping("/checkPicAlbumLike")
    public Result checkPicAlbumLike(@RequestBody Map<String,Object> picAlbumLikeData){
        Integer userId = Integer.parseInt(picAlbumLikeData.get("userId").toString());
        Integer picAlbumId = Integer.parseInt(picAlbumLikeData.get("picAlbumId").toString());
        boolean res = userLikePicAlbumService.isLiked(userId,picAlbumId);
        Map<String,Integer> ans = new HashMap<>();
        if(res){
            ans.put("res",1);
        }else{
            ans.put("res",0);
        }
        ans.put("id",picAlbumId);
        return Result.success(ans);
    }
    @PostMapping("/getCollectSiteListByUserId/{id}")
    public Result getCollectSiteListByUserId(@PathVariable("id") Integer id, @RequestBody Pageparam pageparam){
        Integer userId = id;
        List<CollectBean>collectBeanList = collectService.getCollectSiteListByUserId(userId);
        List<SiteBean>siteBeanList = new ArrayList<>();
        for(int i=0;i<collectBeanList.size();i++){
            siteBeanList.add(siteService.getSiteByNid(collectBeanList.get(i).getSiteId()));
        }
        int pageNum = pageparam.getPagenum();
        int pageSize = pageparam.getPagesize();
        List<SiteBean>ans = new ArrayList<>();
        int startIdx = (pageNum-1)*pageSize;

        for(int i=startIdx;i<min(startIdx+pageSize,siteBeanList.size());i++){
            ans.add(siteBeanList.get(i));
        }
        manualPageVo manualPageVo = new manualPageVo(ans,siteBeanList.size());
        return Result.success(manualPageVo);
    }


}
