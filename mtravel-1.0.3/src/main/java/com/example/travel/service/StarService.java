package com.example.travel.service;

import com.example.travel.mapper.StarMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 杨乐乐
 * @time 2023-04-13 10:06
 */
@Service
public class StarService {
    @Autowired
    StarMapper starMapper;
    public Integer AllCounts(Integer id){
        return starMapper.Allcounts(id);
    }
}
