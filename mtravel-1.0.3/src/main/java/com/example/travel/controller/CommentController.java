package com.example.travel.controller;

import com.example.travel.bean.ArticleBean;
import com.example.travel.bean.CommentBean;
import com.example.travel.bean.UserBean;
import com.example.travel.service.ArticleService;
import com.example.travel.service.CommentService;
import com.example.travel.service.UserService;
import com.example.travel.vo.CommentVo;
import com.example.travel.vo.Pageparam;
import com.example.travel.vo.PageparamAndSearchWord;
import com.example.travel.vo.Result;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.stream.events.Comment;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    CommentService commentService;
    @Autowired
    UserService userService;
    @Autowired
    ArticleService articleService;


    private CommentBean coverAllAttribute(Map<String, Object> params, CommentBean commentBean, boolean isCreate) {
        if (params.get("commentNid") != null && !isCreate) {
            commentBean.setCommentNid(Integer.parseInt(params.get("commentNid").toString()));
        }
        if (params.get("commentContent") != null) {
            commentBean.setCommentContent(params.get("commentContent").toString());
        }
        if (params.get("userId") != null) {
            commentBean.setUserId(Integer.parseInt(params.get("userId").toString()));
        }
        if (params.get("articleId") != null) {
            commentBean.setArticleId(Integer.parseInt(params.get("articleId").toString()));
        }
        if (params.get("rootCommentId") != null) {
            commentBean.setRootCommentId(Integer.parseInt(params.get("rootCommentId").toString()));
        }
        if (params.get("parentCommentId") != null) {
            commentBean.setParentCommentId(Integer.parseInt(params.get("parentCommentId").toString()));
        }
        if (params.get("starCount") != null) {
            commentBean.setStarCount(Integer.parseInt(params.get("starCount").toString()));
        } else {
            if (isCreate) {
                commentBean.setStarCount(0);
            }
        }
        if (params.get("commentCount") != null) {
            commentBean.setCommentCount(Integer.parseInt(params.get("commentCount").toString()));
        } else {
            if (isCreate) {
                commentBean.setCommentCount(0);
            }
        }
        if (params.get("createDate") != null) {
            commentBean.setCreateDate(LocalDateTime.parse(params.get("createDate").toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        } else {
            if (isCreate) {
                commentBean.setCreateDate(LocalDateTime.now());
            }
        }
        return commentBean;
    }

    private CommentVo AssertcommentVo(CommentBean commentBean) {
        return new CommentVo(
                commentBean.getCommentNid(),
                commentBean.getCommentContent(),
                commentBean.getUserId(),
                commentBean.getArticleId(),
                commentBean.getRootCommentId(),
                commentBean.getParentCommentId(),
                commentBean.getStarCount(),
                commentBean.getCommentCount(),
                commentBean.getCreateDate(),
                null,
                null,
                null,
                null);
    }

    private void CommentAboutInfo(CommentVo commentVo){
            if(commentVo.getUserName()==null) {
                UserBean user = userService.getUserByNid(commentVo.getUserId());
                if (user == null) {
                    commentVo.setUserName("用户已注销");
                }
                commentVo.setUserName(user.getUserName());
            }
            ArticleBean article = articleService.getArticleByNid(commentVo.getArticleId());
            if(commentVo.getArticleTitle()==null) {
                if (article == null) {
                    commentVo.setArticleTitle("文章已被删除");
                }
                commentVo.setArticleTitle(article.getArticleTitle());
            }
            if (commentVo.getRootCommentId() == 0)//根评论
            {
                commentVo.setRootComment("root");
            } else {
                CommentBean rootComment = commentService.getCommentByNid(commentVo.getRootCommentId());
                commentVo.setRootComment(rootComment.getCommentContent());
            }
            if (commentVo.getParentCommentId() == 0)//根评论
            {
                commentVo.setParentComment("root");
            } else {
                CommentBean parentComment = commentService.getCommentByNid(commentVo.getParentCommentId());
                commentVo.setParentComment(parentComment.getCommentContent());
            }
    }

    @PostMapping("/getCommentList")
    public Result getCommentList(@RequestBody Pageparam pageparam) {
        PageHelper.startPage(pageparam.getPagenum(), pageparam.getPagesize());
        List<CommentVo> commentVoList = commentService.getCommentList();
//        List<CommentVo>commentVoList = new ArrayList<>();
        if (commentVoList.isEmpty()) {
            return Result.fail(706, "评论列表为空");
        }
        for (CommentVo comment : commentVoList) {
//            commentVo = AssertcommentVo(comment);
            UserBean user = userService.getUserByNid(comment.getUserId());

            if (user == null) {
                comment.setUserName("已注销");
            }
            else {
                comment.setUserName(user.getUserName());
            }


            ArticleBean article = articleService.getArticleByNid(comment.getArticleId());

            if (article == null) {
                return Result.fail(809, "未找到文章");
            }
            comment.setArticleTitle(article.getArticleTitle());
            //TODO:做评论管理时，不需要查看各个评论之间的关系，直接列出所有评论即可
            if (comment.getRootCommentId() == 0)//没有父评论
            {
                comment.setRootComment("root");
            } else {
                CommentBean rootComment = commentService.getCommentByNid(comment.getRootCommentId());
                comment.setRootComment(rootComment.getCommentContent());
            }
            if (comment.getParentCommentId() == 0)//根评论
            {
                comment.setParentComment("root");
            } else {
                CommentBean parentComment = commentService.getCommentByNid(comment.getParentCommentId());
                comment.setParentComment(parentComment.getCommentContent());
            }
        }
        PageInfo<CommentVo> pageInfo = new PageInfo<>(commentVoList);
        return Result.success(pageInfo);

    }

    @PostMapping(value = "/deleteCommentByNid/{id}")
    public Result deleteCommentByNid(@PathVariable("id") Integer id) {
        CommentBean comment = commentService.getCommentByNid(id);
        if (comment != null) {
            int status = commentService.deleteCommentByNid(id);
            if (status == 0) {
                return Result.fail(510, "评论删除失败！");
            }
            return Result.success("删除评论成功");
        } else {
            return Result.fail(509, "评论不存在！");
        }
    }

    @PostMapping(value = "/deleteCommentByParentNid/{id}")
    public Result deleteCommentByParentNid(@PathVariable("id") Integer id) {
        CommentBean commentBean = commentService.getCommentByNid(id);
        int status = commentService.deleteCommentByParentNid(id);
        if (status == 0) {
            return Result.fail(510, "评论删除失败！");
        }
        //如果被删除的不是根评论：
        if(commentBean != null && commentBean.getRootCommentId() != commentBean.getCommentNid()){
            int rootNid = commentBean.getRootCommentId();
            commentBean = commentService.getCommentByNid(rootNid);
            commentBean.setCommentCount(commentBean.getCommentCount() - status);
            commentService.updateComment(commentBean);
        }
        return Result.success("删除评论成功");
    }

    @PostMapping(value = "/deleteCommentByNids")
    public Result deleteCommentByNids(@RequestBody List<CommentBean> commentList) {

        for (CommentBean commentBean : commentList) {

            CommentBean comment = commentService.getCommentByNid(commentBean.getCommentNid());

            if (comment == null) {
                return Result.fail(509, "评论不存在！");
            }

            int status = commentService.deleteCommentByNid(commentBean.getCommentNid());


        }
        return Result.success("删除评论成功");

    }

    @PostMapping("/getCommentListBySearchWord")
    public Result getCommentListBySearchWord(@RequestBody PageparamAndSearchWord pageparamAndSearchWord) {

        String searchWord = pageparamAndSearchWord.getSearchword();
        String value = pageparamAndSearchWord.getValue();
        List<CommentVo> resList = new ArrayList<>();


        switch (value) {
            case "选项1":
//按内容搜索
                PageHelper.startPage(pageparamAndSearchWord.getPagenum(), pageparamAndSearchWord.getPagesize());
                resList=commentService.getCommentListByContent(searchWord);
                for (CommentVo commentVo : resList) {
                    CommentAboutInfo(commentVo);
                }
                break;
            case "选项2":
//按用户名搜索
                UserBean userBean = userService.getUserByName(searchWord);
                if(userBean==null){
                    return  Result.fail(508,"未查询到用户!");
                }
                PageHelper.startPage(pageparamAndSearchWord.getPagenum(), pageparamAndSearchWord.getPagesize());
                resList=commentService.getCommentListByUserId(userBean.getUserNid());
                for (CommentVo commentVo : resList) {
                    commentVo.setUserName(userBean.getUserName());
                    CommentAboutInfo(commentVo);
                }

                break;
            case "选项3":
//按相关文章搜索
                ArticleBean articleBean = articleService.getArticleByTitle(searchWord);
                if(articleBean==null){
                    return Result.fail(509,"为查询到文章，请确认文章标题是否正确！");
                }
                PageHelper.startPage(pageparamAndSearchWord.getPagenum(), pageparamAndSearchWord.getPagesize());
                resList=commentService.getCommentListByArticleId(articleBean.getArticleNid());
                for (CommentVo commentVo : resList) {
                    commentVo.setArticleTitle(articleBean.getArticleTitle());
                    CommentAboutInfo(commentVo);
                }
                break;
        }

        if (resList.isEmpty()) {
            return Result.fail(505, "用户列表为空");
        }


        PageInfo<CommentVo> pageInfo = new PageInfo<>(resList);

        return Result.success(pageInfo);
    }

    @PostMapping(value = "/addComment")
    public Result addComment(@RequestBody Map<String, Object> params) {
        Map<String, Object> comment = (Map<String, Object>) params.get("comment");
        CommentBean commentBean = new CommentBean();
        commentBean = coverAllAttribute(comment,commentBean,true);
        commentService.addComment(commentBean);
        //添加的是根评论：
        if(commentBean.getParentCommentId() == 0){
            int commentId = commentService.getLastNid();
            commentBean.setCommentNid(commentId);
            commentBean.setParentCommentId(commentId);
            commentBean.setRootCommentId(commentId);
            commentService.updateComment(commentBean);
        }else{
            //添加的是子评论：
            commentBean = commentService.getCommentByNid(commentBean.getRootCommentId());
            commentBean.setCommentCount(commentBean.getCommentCount()+1);
            commentService.updateComment(commentBean);
        }
        return Result.success("添加评论成功");
    }

    @GetMapping("/getCommentNum")
    public Result getCommentNum(){
        return Result.success(commentService.getCommentNum());
    }
}
