package com.example.travel.vo;

import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleVo {
    boolean isFollowed;
    boolean liked;
    boolean collected;
    List<FatherCommentVo>fatherCommentList;
}
