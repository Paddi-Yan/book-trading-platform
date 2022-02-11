package com.turing.common;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年02月09日 19:56:24
 */
public enum CommentStatus
{
    NEGATIVE (0,"差评"),
    MEDIUM(1,"中评"),
    POSITIVE (2,"好评");

    private Integer code;
    private String type;

    CommentStatus(Integer code, String status)
    {
        this.code = code;
        this.type = status;
    }

    public Integer getCode()
    {
        return code;
    }

    public String getType()
    {
        return type;
    }
}
