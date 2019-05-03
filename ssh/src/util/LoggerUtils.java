package util;

import java.text.SimpleDateFormat;
import util.ExceptionUtil;

/** Logger(20140207)
 * @author ptchen
 */
public class LoggerUtils {

    /** 輸出一筆console log
     * @param pMessage 提示訊息: null or "":無訊息, others:訊息
     * @param pException Exception
     */
    public static String format(String pMessage, Exception pException) {
        String result = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        result = "[" + sdf.format(System.currentTimeMillis()) + "] ERROR " + pMessage;
        if (pException != null) {
            result += "\n" + ExceptionUtil.getStackTrace(pException);
        }
        return result;
    }

}
