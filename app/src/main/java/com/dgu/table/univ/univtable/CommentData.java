package com.dgu.table.univ.univtable;

public class CommentData {
    public int id;
    public int mid;
    public int aid;
    public String content;
    public String date;
    public String userName;

    public CommentData(int id, int mid, int aid, String content, String date, String userName) {
        this.id = id;
        this.mid = mid;
        this.aid = aid;
        this.content = content;
        this.date = date;
        this.userName = userName;
    }
}