package util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogUtility {
    private static final Logger logger = LogManager.getLogger("SSH");

    public static void fatalLog(String message, Exception ex) {
        logger.fatal(message, ex);
    }

    public static void errorLog(String message, Exception ex) {
        logger.error(message, ex);
    }

    public static void warnLog(String message, Exception ex) {
        logger.warn(message, ex);
    }

    public static void debugLog(String message, Exception ex) {
        logger.debug(message, ex);
    }

    public static void infoLog(String message, Exception ex) {
        logger.info(message, ex);
    }

    public static void fatalLog(String message) {
        logger.fatal(message);
    }

    public static void errorLog(String message) {
        logger.error(message);
    }

    public static void errorLog(Exception ex) {
        logger.error(ex);
    }

    public static void warnLog(String message) {
        logger.warn(message);
    }

    public static void debugLog(String message) {
        logger.debug(message);
    }

    public static void infoLog(String message) {
        logger.info(message);
    }
}
