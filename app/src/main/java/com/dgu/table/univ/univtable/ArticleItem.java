package com.dgu.table.univ.univtable;

public class ArticleItem {
    public int id = 0;
    public int mid = 0;
    public String content = "Content of Article will be displayed in here.";
    public int flag = 0;
    public int hit = 0;
    public int ucode = 0; // SQL
    public String name = "Unknown"; // SQL
    public String date = "2000-10-01T00:00:00.000Z";

    public ArticleItem(){}

    public ArticleItem(int id, int mid, String content, int flag, int hit, int ucode, String name, String date) {
        this.id = id;
        this.mid = mid;
        this.content = content;
        this.flag = flag;
        this.hit = hit;
        this.ucode = ucode;
        this.name = name;
        this.date = date;
    }

    public ArticleItem clone(){
        return new ArticleItem(id, mid, content, flag, hit, ucode, name, date);
    }

}
