package com.example.dayrecord.data;

import java.io.Serializable;

//实现serializable接口方便budle数据的传递
public class User implements Serializable {
    private String id;
    private String password;
    private String nickname;
    private String signature;
    private String headimage;

    public User(String id, String password, String nickname, String signature, String headimage) {
        this.id = id;
        this.password = password;
        this.nickname = nickname;
        this.signature = signature;
        this.headimage = headimage;
    }
    public User() {
    }

    //getter setter
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSignature() {
        return signature;
    }
    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getHeadimage() {
        return headimage;
    }
    public void setHeadimage(String headimage) {
        this.headimage = headimage;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", signature='" + signature + '\'' +
                ", headimage='" + headimage + '\'' +
                '}';
    }

}
