package com.metabubble.BWC.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 配置
 */
@Data
public class Config implements Serializable {
    //序列化
    private static final long serialVersionUID = 1L;

    //配置id
    private Long id;

    //配置名称
    private String name;

    //配置内容
    private String content;

    //配置类型，0为前台配置，1为后台配置
    private Integer type;

}
