package com.example.spring_security_demo.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 用户实体类
 * @Author LinYouRu
 * @Description //TODO
 * @Date 11:41 2019/3/27
 * @Param
 * @return
 **/
@TableName("sys_user")
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;

    private String id;
    @TableField("user_name")
    private String userName;
    @TableField("user_password")
    private String userPassword;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "User{" +
        ", id=" + id +
        ", userName=" + userName +
        ", userPassword=" + userPassword +
        "}";
    }
}
