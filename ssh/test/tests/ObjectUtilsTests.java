package tests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.ObjectUtils;

public class ObjectUtilsTests {
    Object x;

    @Before
    public void Setup() {
        x = new Object() {
            String abc = "asd";
            String qwe = "qwe";
        };
    }

    @After
    public void tearDown() {
        x = null;
    }

    @Test
    public void testGet() {
        String a = ObjectUtils.getField(x, "abc");
        String b = ObjectUtils.getField(x, "qwe");

        Assert.assertEquals("asd", a);
        Assert.assertEquals("qwe", b);
    }

    @Test
    public void testSet() {
        ObjectUtils.setField(x, "xxx", "qwc");
        String a = ObjectUtils.getField(x, "xxx");

        Assert.assertEquals("qwc", a);
    }

    @Test(expected = ClassCastException.class)
    public void testGetCastError() {
        Boolean a = ObjectUtils.getField(x, "abc");
    }
}
