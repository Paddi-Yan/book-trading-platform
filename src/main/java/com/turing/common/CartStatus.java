package com.turing.common;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月30日 17:15:13
 */
public enum CartStatus
{
    DELETED(1,"已被删除");

    private Integer code;
    private String status;

    CartStatus(Integer code, String status)
    {
        this.code = code;
        this.status = status;
    }

    public Integer getCode()
    {
        return code;
    }

    public String getStatus()
    {
        return status;
    }
}
