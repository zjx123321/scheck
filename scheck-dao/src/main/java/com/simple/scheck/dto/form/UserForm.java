package com.simple.scheck.dto.form;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by dell on 2017/5/25.
 */
public class UserForm {

    @NotBlank(message = "请填写用户名")
    @ApiModelProperty(value = "用户名")
    private String name;

    @NotBlank(message = "请输入密码")
    @ApiModelProperty(value = "密码")
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}