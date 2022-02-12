package com.turing.common;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月30日 17:10:39
 */
public enum BookStatus {
    EFFECTIVE(1, "有效"), INVALID(0, "失效");
    private Integer code;
    private String status;

    BookStatus (Integer code, String status) {
        this.code = code;
        this.status = status;
    }

    public Integer getCode () {
        return code;
    }

    public String getStatus () {
        return status;
    }
}
