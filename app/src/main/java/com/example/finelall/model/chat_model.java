package com.example.finelall.model;

public class chat_model {
String msg,from,time,img;

    public chat_model(String msg, String from, String time) {
        this.msg = msg;
        this.from = from;
        this.time = time;
    }

    public chat_model(String msg) {
        this.msg = msg;
    }

    public String getImg() {
        return img;
    }
    public void setImg(String img) {
        this.img = img;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public chat_model() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public chat_model(String msg, String from, String time,String img) {
        this.msg = msg;
        this.from = from;
        this.time = time;
        this.img=img;
    }
}
