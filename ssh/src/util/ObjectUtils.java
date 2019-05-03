package util;

import java.lang.reflect.Field;

public class ObjectUtils {
    private ObjectUtils() {
    }

    public static <T> T getField(Object obj, String key) {
        try {
            Field field = obj.getClass().getDeclaredField(key);
            field.setAccessible(true);
            return (T) field.get(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> void setField(Object obj, String key, T val) {
        try {
            Field field = obj.getClass().getDeclaredField(key);
            field.setAccessible(true);
            field.set(obj, val);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
