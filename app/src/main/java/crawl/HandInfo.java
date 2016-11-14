package crawl;

public class HandInfo {

    public HandInfo(){}
    public HandInfo(String no, String subject, String name, String date, String handin, String open, String score){
        this.no = no;
        this.subject = subject;
        this.name = name;
        this.date = date;
        this.handin = handin;
        this.open = open;
        this.score = score;
    }

    public String no, subject, name, date, handin, open, score;
}