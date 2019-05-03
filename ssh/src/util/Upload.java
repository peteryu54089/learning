package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ep.CryptographyLib;
import model.Account;
import model.DownloadFileRequest;
import model.HasSchoolSchema;
import model.UploadFile;
import org.apache.commons.io.IOUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

public class Upload {
    private static volatile File saveRootDir = null;

    @Deprecated
    public Upload(final ServletContext servletContext) {
        getSaveRootDir(servletContext);
    }

    @Deprecated
    public static File getSaveRootDir(final ServletContext servletContext) {
        final String saveRoot = "WEB-INF/storage/";
//        final String saveRoot = "WEB-INF/stu_learning_files/";
        if (saveRootDir == null) {
            synchronized (Upload.class) {
                saveRootDir = new File(servletContext.getRealPath(saveRoot));
            }
        }

        return saveRootDir;
    }

    @Deprecated
    public static File getAccountBaseDir(final Account account) {
        return getAccountBaseDir(account.getIdNumber());
//        return getAccountBaseDir(account.getSchoolSchema(), account.getIdNumber());
    }

    @Deprecated
    public static File getAccountBaseDir(String pSch_code, String pIdno) {
        if (saveRootDir == null)
            throw new NullPointerException();

        return new File(saveRootDir, (pSch_code + File.separator + pIdno));
    }

    @Deprecated
    public static File getAccountBaseDir(final String account) {
        if (saveRootDir == null)
            throw new NullPointerException();

        return new File(saveRootDir, "files" + File.separator + account);
    }

    @Deprecated
    public String uploadFile(final Account account, final Part file, final String extension, final String order) throws Exception {
        String fileName = (new Date()).getTime() + "_" + account.getIdNumber() + "_" + order + "." + extension;
        File dir = getAccountBaseDir(account);
        // 實際檔案儲存位置
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String pathSaving = dir.getAbsolutePath() + File.separator + fileName;

        file.write(pathSaving);
        return fileName;
    }

    @Deprecated
    public String uploadFile(final Account account, final Part file, final String extension) throws Exception {
        String fileName = (new Date()).getTime() + "_" + account.getIdNumber() + "." + extension;
        File dir = getAccountBaseDir(account);
        // 實際檔案儲存位置
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String pathSaving = dir.getAbsolutePath() + File.separator + fileName;

        file.write(pathSaving);
        return fileName;
    }

    @Deprecated
    /*學生基本資料維護功能不開放所以用不到*/
    public String uploadFile(final File baseDir, final Part file) throws IOException {
        File dir = new File(saveRootDir, baseDir.getPath());
        // 實際檔案儲存位置
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fileName = UUID.randomUUID().toString();
        file.write(new File(dir, fileName).getAbsolutePath());
        return fileName;
    }

    @Deprecated
    /*學校基本資料維護功能不開放所以用不到*/
    public String uploadFile(final String baseDir, final Part file) throws IOException {
        return uploadFile(new File(baseDir), file);
    }

    @Deprecated
    public String uploadFile(final File baseDir, final InputStream is) throws IOException {
        File dir = new File(saveRootDir, baseDir.getPath());
        // 實際檔案儲存位置
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fileName = UUID.randomUUID().toString();
        try (OutputStream fs = new FileOutputStream(new File(dir, fileName))) {
            IOUtils.copy(is, fs);
        }
        return fileName;
    }

    public static String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] tokens = contentDisp.split(";");
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length() - 1);
            }
        }
        return "";
    }

    public static String getExtension(String filename) {
        int startIndex = filename.lastIndexOf(46);
        int endIndex = filename.length();
        return filename.substring(startIndex, endIndex).toLowerCase();
    }

    private static final String DOWNLOAD_KEY = "n+w/dxogTY9CYb+mR9eMEcZCx5c738VmstnRHiI";
    private static final String DOWNLOAD_KEY_DIM = "-";

    private static final Gson getDownloadLinkGson() {
        return new GsonBuilder().create();
    }

    public static String generateDownloadLink(final HttpServletRequest request, DownloadFileRequest downloadFileRequestRequest) {
        Gson gson = getDownloadLinkGson();
        String root = "";
        String jsonStr = gson.toJson(downloadFileRequestRequest);
        if (request != null) {
            StringBuffer url = request.getRequestURL();
            String uri = request.getRequestURI();
            String ctx = request.getContextPath();
            root = url.substring(0, url.length() - uri.length() + ctx.length());
        } else {
            root = ".";
        }

        Cipher cipher = CryptographyLib.getCipher(DOWNLOAD_KEY, Cipher.ENCRYPT_MODE);
        byte[] hash = CryptographyLib.sha256(jsonStr);
        String token = "";
        try {
            String dataStr = CryptographyLib.encodeBase64(cipher.doFinal(jsonStr.getBytes(StandardCharsets.UTF_8)))
                    + DOWNLOAD_KEY_DIM + CryptographyLib.encodeBase64(hash);
            token = URLEncoder.encode(dataStr, String.valueOf(StandardCharsets.UTF_8));
        } catch (IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException | NullPointerException e) {
            e.printStackTrace();
        }

        return root + "/externalDownloadLink?d=" + token;
    }

    public static String generateDownloadLink(final HttpServletRequest request, HasSchoolSchema hasSchoolSchema, UploadFile uploadFile) {
        DownloadFileRequest downloadFileRequestRequest = new DownloadFileRequest(
                hasSchoolSchema.getSchoolSchema(),
                uploadFile.getId(),
                null,
                uploadFile.getFilePath(),
                uploadFile.getFileName()
        );

        return generateDownloadLink(request, downloadFileRequestRequest);
    }

    public static DownloadFileRequest decodeDownloadLink(String token) {
        Cipher cipher = CryptographyLib.getCipher(DOWNLOAD_KEY, Cipher.DECRYPT_MODE);
        try {
            String[] info = token.split(DOWNLOAD_KEY_DIM);
            byte[] encData = CryptographyLib.decodeBase64(info[0]);
            byte[] hash = CryptographyLib.decodeBase64(info[1]);

            byte[] data = cipher.doFinal(encData);

            byte[] recDataHash = CryptographyLib.sha256(data);

            if (!Arrays.equals(hash, recDataHash))
                return null;

            String json = new String(data, StandardCharsets.UTF_8);
            Gson gson = getDownloadLinkGson();
            DownloadFileRequest ret = gson.fromJson(json, DownloadFileRequest.class);

            return ret;
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
