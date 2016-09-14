package weekview;

import java.util.Calendar;
import java.util.List;

public interface WeekViewLoader {
    double toWeekViewPeriodIndex(Calendar instance);
    List<? extends WeekViewEvent> onLoad(int periodIndex);
}
