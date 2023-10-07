package com.example.travel.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 杨乐乐
 * @time 2023-04-07 20:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagsForArticleBean {
    private Integer tagArticleNid;
    private String  articleTag;
}
