package com.example.travel.mapper;

import org.apache.ibatis.annotations.Mapper;

/**
 * @author 杨乐乐
 * @time 2023-04-13 10:07
 */
@Mapper
public interface StarMapper {
    public Integer Allcounts(Integer articleNid);
}
