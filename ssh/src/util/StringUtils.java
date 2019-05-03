package util;

//import org.apache.tomcat.util.security.Escape;

import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;

public class StringUtils {
    private StringUtils() {
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static String extractPair(String str, String begin, String end) {
        int startIdx = str.indexOf(begin) + (begin).length();
        int endIdx = str.indexOf(end, startIdx + 1);

        return str.substring(startIdx, endIdx);
    }

    public static String replaceTemplate(String template, Map<String, String> map) {
        for (Map.Entry<String, String> e : map.entrySet()) {
            template = template.replaceAll(
                    Pattern.quote("{" + e.getKey() + "}"),
//                    Escape.htmlElementContent(e.getValue())
                    StringEscapeUtils.escapeHtml(e.getValue()) 
            );
        }

        return template;
    }

}
