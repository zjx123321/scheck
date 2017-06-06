package com.simple.scheck.dto.form;

import com.simple.scheck.dto.entity.User;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by dell on 2017/5/25.
 */
public class UserForm {

    private Integer id;

    @NotBlank(message = "请填写用户名")
    @ApiModelProperty(value = "用户名")
    private String name;

    @NotBlank(message = "请输入密码")
    @ApiModelProperty(value = "密码")
    private String password;

    public User toUser() {
        User user = new User();
        user.setId(this.id);
        user.setName(this.name);
        user.setPassword(this.password);
        return user;
    }

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", name=").append(name);
        sb.append(", password=").append(password);
        sb.append("]");
        return sb.toString();
    }

}