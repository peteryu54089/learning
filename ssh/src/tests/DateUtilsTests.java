package tests;

import org.junit.Assert;
import org.junit.Test;
import util.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class DateUtilsTests {
    @Test
    public void testDateFormat() {
        testDateFormat("2018/09/21", "107/09/21");
        testDateFormat("2000/4/5", "89/04/05");
    }

    @Test
    public void testDateTimeFormat() {
        testDateTimeFormat("2018/09/21 12:54", "107/09/21 12:54");
        testDateTimeFormat("2000/4/5 9:8", "89/04/05 09:08");
    }

    private void testDateFormat(String input, String expected) {
        try {
            Date now = new SimpleDateFormat("yyyy/MM/dd").parse(input);
            String mgStr = DateUtils.formatDate(now);
            assertEquals(expected, mgStr);
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    private void testDateTimeFormat(String input, String expected) {
        try {
            Date now = new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(input);
            String mgStr = DateUtils.formatDateTime(now);
            assertEquals(expected, mgStr);
        } catch (ParseException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
