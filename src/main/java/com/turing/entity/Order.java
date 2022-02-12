package com.turing.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.turing.common.OrderStatus;
import com.turing.entity.dto.OrderDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年02月10日 10:40:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "orders")
public class Order {

    @TableId
    private Long id;

    private String cover;

    private String title;

    private BigDecimal total;

    private BigDecimal price;

    private Integer count;

    private Integer bookId;
    /**
     * 收货地址编号
     */
    private Integer addressId;

    private LocalDateTime createdTime;

    private LocalDateTime payTime;

    private Long userId;
    /**
     * 运费
     */
    private BigDecimal freight;

    /**
     * 订单状态
     * -1-订单被取消或未在按时时间内支付
     * 0-创建成功,待支付
     * 1-支付成功,待发货
     * 2-已发货
     * 3-已收货-订单结束
     */
    private Integer status;


    public void transform (OrderDto orderDto) {
        BeanUtils.copyProperties(orderDto, this);
        switch (orderDto.getStatus()) {
            case "订单被取消或未在十五分钟内支付自动取消":
                this.setStatus(OrderStatus.CANCELED.getCode());
                break;
            case "订单创建成功,等待支付,十五分钟内未完成支付订单自动取消":
                this.setStatus(OrderStatus.TO_BE_PAID.getCode());
                break;
            case "订单支付成功,等待发货":
                this.setStatus(OrderStatus.TO_BE_SHIPPED.getCode());
                break;
            case "订单已发货":
                this.setStatus(OrderStatus.SHIPPED.getCode());
                break;
            case "订单已签收,等待确认收货":
                this.setStatus(OrderStatus.SHIPPED_END.getCode());
                break;
            case "订单已确认收货,订单结束":
                this.setStatus(OrderStatus.RECEIVED.getCode());
                break;
        }
    }
}