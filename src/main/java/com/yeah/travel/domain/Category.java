package com.yeah.travel.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Category implements Serializable {
    private int cid;//分类id
    private String cname;//分类名称
}
