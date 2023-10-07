package com.example.travel.vo;

import lombok.Data;
@Data
public class PageparamAndSearchWord {
    private Integer pagesize;
    private Integer pagenum;

    private String searchword;
    private String value;
}
