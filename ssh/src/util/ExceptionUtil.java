package util;

import java.io.PrintWriter;
import java.io.StringWriter;


/** 例外處理(20140207)
 * @author ptchen
 */
public class ExceptionUtil {

    /** 取得Exception的Stack Trace
     * @param pException Exception
     * @return Exception Stack Trace
     */
    public static String getStackTrace(Exception pException) {
        StringWriter stringWriter = null;

        if (pException != null) {
            stringWriter = new StringWriter();
            pException.printStackTrace(new PrintWriter(stringWriter));
            return stringWriter.toString();
        }
        return "";
    }
}
