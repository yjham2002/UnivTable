package weekview;

import java.util.Calendar;
import java.util.List;

public interface WeekViewLoader {

    /********************************************************
     * This codes are referenced by Github(https://github.com/alamkanak/Android-Week-View)
     ********************************************************/

    double toWeekViewPeriodIndex(Calendar instance);
    List<? extends WeekViewEvent> onLoad(int periodIndex);
}
