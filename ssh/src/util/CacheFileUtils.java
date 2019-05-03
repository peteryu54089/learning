package util;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class CacheFileUtils {
    public static final String PATH = ".cache";

    private CacheFileUtils() {
    }

    public static File createCacheFile(final ServletContext servletContext, final String ext) {
        String fileName = UUID.randomUUID().toString() + ext;
        File file = new File(servletContext.getRealPath(PATH), fileName);
        file.deleteOnExit();
        System.out.println("Temp file created: " + file.getAbsolutePath());
        return file;
    }

    public static void sendRedirect(final HttpServletResponse response, File file) throws IOException {
        response.sendRedirect("/" + PATH + "/" + file.getName());
    }
}

