package com.turing.common;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年02月10日 10:47:54
 */
public enum OrderStatus
{
    CANCELED(-1,"订单被取消或未在三十分钟内支付自动取消"),
    TO_BE_PAID(0,"订单创建成功,等待支付,三十分钟内未完成支付订单自动取消"),
    TO_BE_SHIPPED(1,"订单支付成功,等待发货"),
    SHIPPED(2,"订单已发货"),
    SHIPPED_END(3,"订单已签收,等待确认收货"),
    RECEIVED(4,"订单已确认收货,订单结束");

    private Integer code;
    private String status;

    OrderStatus(Integer code, String status)
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
