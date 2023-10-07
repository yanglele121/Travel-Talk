package com.example.travel.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminBean {
  private Integer adminNid;
  private String adminName;
  private String adminPassword;
}
