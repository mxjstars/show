package com.mingrisoft.model;

import java.util.Date;

public class TbShowUser {
    private String userId;

    private String userName;

    private String userPwd;

    private String nickName;

    private String phone;

    private String email;

    private String qq;

    private String wechat;

    private String sinaBlog;

    private Integer gender;

    private Integer integral;

    private Integer userTypeCodeId;

    private Date addtime;

    private Integer stateCodeId;

    private String headimg;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd == null ? null : userPwd.trim();
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq == null ? null : qq.trim();
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat == null ? null : wechat.trim();
    }

    public String getSinaBlog() {
        return sinaBlog;
    }

    public void setSinaBlog(String sinaBlog) {
        this.sinaBlog = sinaBlog == null ? null : sinaBlog.trim();
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public Integer getUserTypeCodeId() {
        return userTypeCodeId;
    }

    public void setUserTypeCodeId(Integer userTypeCodeId) {
        this.userTypeCodeId = userTypeCodeId;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public Integer getStateCodeId() {
        return stateCodeId;
    }

    public void setStateCodeId(Integer stateCodeId) {
        this.stateCodeId = stateCodeId;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg == null ? null : headimg.trim();
    }
}