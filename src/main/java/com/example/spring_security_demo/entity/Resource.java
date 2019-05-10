package com.example.spring_security_demo.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * <p>
 * 
 * </p>
 *
 * @author lyr123
 * @since 2019-03-28
 */
@TableName("sys_resource")
public class Resource extends Model<Resource> {

    private static final long serialVersionUID = 1L;

    private String id;
    private String priority;
    @TableField("res_code")
    private String resCode;
    @TableField("res_name")
    private String resName;
    private String url;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Resource{" +
        ", id=" + id +
        ", priority=" + priority +
        ", resCode=" + resCode +
        ", resName=" + resName +
        ", url=" + url +
        "}";
    }
}
