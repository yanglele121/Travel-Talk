package com.example.travel.mapper;

import com.example.travel.bean.CommentBean;
import com.example.travel.vo.CommentVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    List<CommentVo> getCommentList();
    CommentBean getCommentByNid(int commentNid);
    List<CommentBean> getCommentListByParentNid(int parentCommentId);
    List<CommentBean> getSonCommentListByRootNid(int rootCommentId);
    List<CommentBean> getRootCommentListByArticleNid(int articleId);
    List<CommentVo> getCommentListByUserId(int userId);
    List<CommentVo> getCommentListByArticleId(Integer articleId);
    int addComment(CommentBean commentBean);
    int updateComment(CommentBean commentBean);
    int deleteCommentByNid(int commentNid);
    int deleteCommentByParentNid(int parentCommentId);
    int deleteCommentByArticleNid(int articleId);

    List<CommentVo> getCommentListByContent(String searchWord);

    int getLastNid();
    List<Integer> getCommentNum();
}
