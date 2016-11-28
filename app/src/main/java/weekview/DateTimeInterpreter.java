package weekview;

import java.util.Calendar;

public interface DateTimeInterpreter {

    /********************************************************
     * This codes are referenced by Github(https://github.com/alamkanak/Android-Week-View)
     ********************************************************/

    String interpretDate(Calendar date);
    String interpretTime(int hour);
}
