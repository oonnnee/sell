package com.imooc.sell.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class OrderForm {

    @NotEmpty(message = "订单人姓名不能为空")
    private String name;

    @NotEmpty(message = "订单人电话不能为空")
    private String phone;

    @NotEmpty(message = "订单人地址不能为空")
    private String address;

    @NotEmpty(message = "订单人openid不能为空")
    private String openid;

    @NotEmpty(message = "订单不能为空")
    private String items;
}
