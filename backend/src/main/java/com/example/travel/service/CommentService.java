package com.example.travel.service;

import com.example.travel.bean.CommentBean;
import com.example.travel.mapper.CommentMapper;
import com.example.travel.mapper.UserLikeCommentMapper;
import com.example.travel.vo.CommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

@Service
public class CommentService {
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    UserLikeCommentMapper userLikeCommentMapper;
    private Queue<Integer>q = new ArrayDeque<>();
    public List<CommentVo> getCommentList(){
        return commentMapper.getCommentList();
    };
    public CommentBean getCommentByNid(int commentNid){
        return commentMapper.getCommentByNid(commentNid);
    };
    public List<CommentBean> getSonCommentListByRootNid(int rootCommentNid){
        return commentMapper.getSonCommentListByRootNid(rootCommentNid);
    };
    public List<CommentBean> getRootCommentListByArticleId(int articleId){
        return commentMapper.getRootCommentListByArticleNid(articleId);
    };
    public List<CommentVo> getCommentListByUserId(int userId){
        return commentMapper.getCommentListByUserId(userId);
    };
    public List<CommentVo> getCommentListByArticleId(Integer articleId){
        return commentMapper.getCommentListByArticleId(articleId);
    }
    public int addComment(CommentBean commentBean){
        return commentMapper.addComment(commentBean);
    };
    public int updateComment(CommentBean commentBean){
        return commentMapper.updateComment(commentBean);
    };
    public int deleteCommentByNid(int commentNid){
        return commentMapper.deleteCommentByNid(commentNid);
    };
    public int deleteCommentByParentNid(int parentCommentNid){
        int status =0 ;
        q.add(parentCommentNid);
        while(!q.isEmpty()){
            int x = q.element();
            q.remove();
            status += commentMapper.deleteCommentByNid(x);
            userLikeCommentMapper.delComment(x);
            List<CommentBean> sonCommentList = commentMapper.getCommentListByParentNid(x);
            for(int i=0;i< sonCommentList.size();i++){
                q.add(sonCommentList.get(i).getCommentNid());
            }
        }
        return status;
    };
    public int deleteCommentByArticleNid(int articleNid){
        return commentMapper.deleteCommentByArticleNid(articleNid);
    };

    public List<CommentVo> getCommentListByContent(String searchWord) {
        return commentMapper.getCommentListByContent(searchWord);
    }

    public int getLastNid(){
        return commentMapper.getLastNid();
    }

    public Integer getCommentNum() {
        return commentMapper.getCommentNum().get(0);
    }
}
