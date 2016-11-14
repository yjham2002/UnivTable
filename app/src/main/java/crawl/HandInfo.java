package crawl;

public class HandInfo {

    public String no = "과제번호";
    public String subject = "과목명";
    public String name = "과제명";
    public String date = "일시정보";
    public String handin = "제출여부정보";
    public String open = "점수공개정보";
    public String score = "점수정보";

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

    public HandInfo clone(){
        return new HandInfo(no, subject, name, date, handin, open, score);
    }

}