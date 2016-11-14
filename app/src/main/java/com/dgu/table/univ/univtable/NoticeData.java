package com.dgu.table.univ.univtable;

public class NoticeData {
    public int id = -1;

    public NoticeData(int id, String Title, String Content, String Date){
        this.id = id;
        this.Title = Title;
        this.Content = Content;
        this.Date = Date;
    }

    public String Title = "";
    public String Content = "";
    public String Date = "";
}