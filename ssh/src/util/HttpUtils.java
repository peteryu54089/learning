package util;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by user on 2018/10/21.
 */
public class HttpUtils {
    private HttpUtils() {

    }

    public static String getMime(Path path) {
        try {
            return Files.probeContentType(path);
        } catch (IOException e) {
            e.printStackTrace();
            return "application/octet-stream";
        }
    }

    public static void sendFile(final HttpServletResponse response, final File file, String fileName) throws IOException {
        String mimeType = getMime(file.toPath());
        // response.setContentLengthLong(file.length());
        try (InputStream in = new FileInputStream(file)) {
            sendFile(response, in, mimeType, fileName);
        }
    }

    public static void sendFile(final HttpServletResponse response, final File file) throws IOException {
        sendFile(response, file, file.getName());
    }

    public static void sendFile(final HttpServletResponse response, InputStream in) throws IOException {
        sendFile(response, in, "application/octet-stream", "data.bin");
    }

    public static void sendFile(final HttpServletResponse response, InputStream in, String mimeType, String fileName) throws IOException {
        response.setContentType(mimeType);
        sendFileName(response, fileName);
        try (OutputStream out = response.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int n;
            while ((n = in.read(buffer)) != -1) {
                out.write(buffer, 0, n);
            }
        }
    }

    public static void sendFile(final HttpServletResponse response, ByteArrayOutputStream os, String mimeType, String fileName) throws IOException {
        response.setContentType(mimeType);
        sendFileName(response, fileName);

        byte[] data = os.toByteArray();

        try (OutputStream out = response.getOutputStream()) {
            out.write(data, 0, data.length);
        }
    }

    public static void sendDownloadFile(final HttpServletResponse response, final File file, String originalName) throws IOException {
        sendFile(response, file, originalName);
    }

    private static void sendFileName(final HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
        String encodedFileName = java.net.URLEncoder.encode(fileName, StandardCharsets.UTF_8.displayName()).replace("+", "%20");

        response.setHeader("Content-Disposition", "attachment; " +
                "filename=" + encodedFileName + "; " +
                "filename*=UTF-8''" + encodedFileName
        );
    }
}
