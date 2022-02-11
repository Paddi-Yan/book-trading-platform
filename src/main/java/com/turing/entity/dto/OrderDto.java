package com.turing.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.turing.common.OrderStatus;
import com.turing.entity.Order;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: 又蠢又笨的懒羊羊程序猿
 * @CreateTime: 2022年02月10日 10:58:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDto
{
    @ApiModelProperty(hidden = true)
    private Long id;

    @ApiModelProperty(hidden = true)
    private String cover;

    @ApiModelProperty(hidden = true)
    private String title;

    @ApiModelProperty(hidden = true)
    private BigDecimal total;

    @ApiModelProperty(hidden = true)
    private BigDecimal price;

    private Integer count;

    private Integer bookId;

    private Long userId;

    @ApiModelProperty(required = false,name = "cartId",value = "如果是从购物车提交订单需携带该参数",hidden = true)
    private Long cartId;

    private Integer addressId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @ApiModelProperty(hidden = true)
    private LocalDateTime createdTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @ApiModelProperty(hidden = true)
    private LocalDateTime payTime;

    /**
     * 运费
     */
    @ApiModelProperty(hidden = true)
    private BigDecimal freight;

    /**
     * 订单状态
     * -1-订单被取消或未在按时时间内支付
     * 0-创建成功,待支付
     * 1-支付成功,待发货
     * 2-已发货
     * 3-已收货-订单结束
     */
    @ApiModelProperty(hidden = true)
    private String status;

    public void transform(Order order)
    {

        BeanUtils.copyProperties(order,this);
        switch (order.getStatus())
        {
            case -1:
                this.setStatus(OrderStatus.CANCELED.getStatus());
                break;
            case 0:
                this.setStatus(OrderStatus.TO_BE_PAID.getStatus());
                break;
            case 1:
                this.setStatus(OrderStatus.TO_BE_SHIPPED.getStatus());
                break;
            case 2:
                this.setStatus(OrderStatus.SHIPPED.getStatus());
                break;
            case 3:
                this.setStatus(OrderStatus.SHIPPED_END.getStatus());
                break;
            case 4:
                this.setStatus(OrderStatus.RECEIVED.getStatus());
                break;
        }
    }
}
