package crawl;

public class ClassInfo {
    public static final int NULLPTR = -1;

    public String title = "강의 정보"; // title of Lecture

    public String location = "강의실 정보"; // Location of Lecture
    public String rawtime = "시간 정보"; // time in raw state
    public int weekDay = NULLPTR;
    public int startHour = NULLPTR;
    public int startMin = NULLPTR;
    public int endHour = NULLPTR;
    public int endMin = NULLPTR;

    public ClassInfo(){}

    public ClassInfo(String title, String location, String rawtime, int weekDay, int startHour, int startMin, int endHour, int endMin) {
        this.title = title;
        this.location = location;
        this.rawtime = rawtime;
        this.weekDay = weekDay;
        this.startHour = startHour;
        this.startMin = startMin;
        this.endHour = endHour;
        this.endMin = endMin;
    }

    public ClassInfo clone(){
        return new ClassInfo(title, location, rawtime, weekDay, startHour, startMin, endHour, endMin);
    }
}
