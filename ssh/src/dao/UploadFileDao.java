package dao;

import model.Semester;
import model.UploadFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UploadFileDao {
    UploadFile insert(String baseDir, String schoolId,
                      final Semester sem,
                      Integer rgno,
                      String originalFilename,
                      InputStream is);

    UploadFile insert(String baseDir, String schoolId,
                      final Semester sem,
                      String staffCode,
                      String originalFilename,
                      InputStream is);

    UploadFile getById(int id);

    List<UploadFile> getByIds(List<Integer> ids);

    default Map<Integer, UploadFile> getByIdsToMap(List<Integer> ids) {
        Map<Integer, UploadFile> fileMap = new HashMap<>();
        for (UploadFile file : getByIds(ids)) {
            fileMap.put(file.getId(), file);
        }
        return fileMap;
    }

    boolean deleteById(Integer id);
}
