package servlet;

import dao.UploadFileDao;
import model.Account;
import model.PerformanceBaseModel;
import model.SystemConfig;
import model.UploadFile;
import util.FilePartUtils;
import util.Upload;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.util.Map;
import java.util.Set;

public class _BasePerformanceServlet extends _BaseServlet {
    protected Part checkDocFile(SystemConfig.Performance perfConfig, Part file) throws FileCheckException {
        if (uploadFile(file, perfConfig.getMaxDocSize(), perfConfig.getAllowDocTypes())) {
            return file;
        }

        return null;
    }

    protected Part checkVideoFile(SystemConfig.Performance perfConfig, Part file) throws FileCheckException {
        if (uploadFile(file, perfConfig.getMaxVideoSize(), perfConfig.getAllowVideoTypes())) {
            return file;
        }

        return null;
    }

    private boolean uploadFile(Part file, int maxSize, Set<String> allowExts) throws FileCheckException {
        if (file == null || file.getSize() == 0) {
            return false;
        }
        if (file.getSize() > maxSize) {
            throw new FileCheckException("檔案過大");
        }

        String fileExt = Upload.getExtension(FilePartUtils.getSubmittedFileName(file));
        if (!allowExts.contains(fileExt)) {
            throw new FileCheckException("檔案格式錯誤");
        }

        return true;
    }

    protected void updateOldPerformanceModel(UploadFileDao uploadFileDao, PerformanceBaseModel model, UploadFile docFile, UploadFile videoFile) {
        if (docFile != null) {
            uploadFileDao.deleteById(model.getDocumentFileId());
            model.setDocumentFileId(docFile.getId());
        }
        if (videoFile != null) {
            uploadFileDao.deleteById(model.getVideoFileId());
            model.setVideoFileId(videoFile.getId());
        }
    }

    protected void setInfoMap(HttpServletRequest request, Account account,
                              Map<String, String> options, PerformanceBaseModel model) {
        if (model.getDocumentFile() != null) {
            options.put("document_original_filename", model.getDocumentFile().getFileName());
            options.put("document_link", Upload.generateDownloadLink(request, account, model.getDocumentFile()));
        } else {
            options.put("document_original_filename", "");
            options.put("document_link", "#");
        }

        if (model.getVideoFile() != null) {
            options.put("video_original_filename", "");
            options.put("video_link", "#");
        }


    }

    protected class FileCheckException extends Exception {
        public FileCheckException(String message) {
            super(message);
        }
    }
}
