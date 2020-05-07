package com.example.dayrecord.data;

public class Comment {

    private int id;
    private int num;
    private String content;
    private String userid;
    private String time;
    private String nickname;
    private int sharingid;
    private String headimage;

    public Comment(int id, int num, String content, String userid, String time, String nickname, int sharingid, String headimage) {
        this.id = id;
        this.num = num;
        this.content = content;
        this.userid = userid;
        this.time = time;
        this.nickname = nickname;
        this.sharingid = sharingid;
        this.headimage = headimage;
    }
    public Comment() {
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSharingid() {
        return sharingid;
    }

    public void setSharingid(int sharingid) {
        this.sharingid = sharingid;
    }

    public String getHeadimage() {
        return headimage;
    }

    public void setHeadimage(String headimage) {
        this.headimage = headimage;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", num=" + num +
                ", content='" + content + '\'' +
                ", userid='" + userid + '\'' +
                ", time='" + time + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sharingid='" + sharingid + '\'' +
                ", headimage='" + headimage + '\'' +
                '}';
    }
}
