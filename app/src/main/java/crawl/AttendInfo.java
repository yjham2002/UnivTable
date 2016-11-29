package crawl;

public class AttendInfo {
    public String subject = "결석 과목명";
    public String rawDate = "일시정보";

    public AttendInfo(){}

    public AttendInfo(String subject, String rawDate) {
        this.subject = subject;
        this.rawDate = rawDate;
    }
}
