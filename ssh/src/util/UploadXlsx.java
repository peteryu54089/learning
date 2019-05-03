package util;

import javax.servlet.http.Part;
import java.io.File;

public class UploadXlsx {

    private final String saveRoot = "file/";

    public String uploadFile(final Part file) throws Exception {
        String fileName = Upload.getFileName(file);
        // 實際檔案儲存位置

        File dir = new File(saveRoot);
        //if (!fileName.toLowerCase().endsWith(".xlsx") && !fileName.toLowerCase().endsWith(".xlsx")) { // 檢查副檔名
        //throw new Exception();
        //}
        if (!dir.exists())
            dir.mkdirs();
        String pathSaving = dir.getAbsolutePath() + File.separator + fileName;
        System.out.println(pathSaving);
        file.write(pathSaving);
        return fileName;
    }

    public String getSaveRoot() {
        return saveRoot;
    }
}
