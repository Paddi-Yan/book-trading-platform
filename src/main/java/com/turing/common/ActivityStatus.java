package com.turing.common;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年01月29日 16:50:30
 */
public enum ActivityStatus {
    EXAMINE(0, "审核中"), EFFECTIVE(1, "有效"), INVALID(-1, "失效");
    private Integer code;
    private String status;

    ActivityStatus (Integer code, String status) {
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
