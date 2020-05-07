package com.example.dayrecord.data;

public class Sharings {

    private int id;
    private int num;
    private int commentnum;
    private String content;
    private String author;
    private String userid;
    private String time;
    private String nickname;
    private String image;
    private String headimage;

    public Sharings(int id, int num, int commentnum, String content, String author, String userid, String time, String nickname, String image, String headimage) {
        this.id = id;
        this.num = num;
        this.commentnum = commentnum;
        this.content = content;
        this.author = author;
        this.userid = userid;
        this.time = time;
        this.nickname = nickname;
        this.image = image;
        this.headimage = headimage;
    }
    public Sharings() {
    }

    public int getNum() {
        return num;
    }

    @Override
    public String toString() {
        return "Sharings{" +
                "id=" + id +
                ", num=" + num +
                ", content='" + content + '\'' +
                ", author='" + author + '\'' +
                ", userid='" + userid + '\'' +
                ", time='" + time + '\'' +
                ", nickname='" + nickname + '\'' +
                ", image='" + image + '\'' +
                ", headimage='" + headimage + '\'' +
                '}';
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getHeadimage() {
        return headimage;
    }

    public void setHeadimage(String headimage) {
        this.headimage = headimage;
    }

    public int getCommentnum() {
        return commentnum;
    }

    public void setCommentnum(int commentnum) {
        this.commentnum = commentnum;
    }
}
