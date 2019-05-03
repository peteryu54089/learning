package dao.impl;

import dao.StudentDao;
import dao.UploadFileDao;
import model.HasSchoolSchema;
import model.PerformanceBaseModel;
import model.UploadFile;
import model.role.Student;
import util.Upload;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BasePerformanceDao extends BaseDao {
    protected BasePerformanceDao(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
    }

    protected int setFilesAndReturnNewIndex(
            int i,
            final PreparedStatement ps,
            final PerformanceBaseModel model
    ) {
        BiConsumer<Integer, Integer> setter = (idx, val) -> {
            try {
                if (val == null) {
                    ps.setNull(idx, Types.INTEGER);
                } else {
                    ps.setInt(idx, val);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };

        setter.accept(++i, model.getDocumentFileId());
        setter.accept(++i, model.getVideoFileId());

        return i;
    }

    protected void deleteOldUploadedFile(PerformanceBaseModel oldModel, PerformanceBaseModel newModel) {
        List<Integer> pendingDeleteFileIds = new LinkedList<>();

        if (
                newModel == null || (
                        oldModel.getDocumentFileId() != null
                                && !oldModel.getDocumentFileId().equals(newModel.getDocumentFileId())
                )
        )
            pendingDeleteFileIds.add(oldModel.getDocumentFileId());

        if (
                newModel == null || (
                        oldModel.getVideoFileId() != null
                                && !oldModel.getVideoFileId().equals(newModel.getVideoFileId())
                )
        )
            pendingDeleteFileIds.add(oldModel.getVideoFileId());

        UploadFileDao fileDao = new UploadFileDaoImpl(this);
        pendingDeleteFileIds = pendingDeleteFileIds.stream().filter(Objects::nonNull).collect(Collectors.toList());
        for (int id : pendingDeleteFileIds) {
            fileDao.deleteById(id);
        }
    }

    /*
    "DOCUMENT_FILE_ID = ?,VIDEO_FILE_ID = ?,EXTERNAL_LINK= ?," +
    "SOURCE = ?, SUBMITTER = ?, CONTENT = ? ,CHECK = ? , STATUS = ?,SELECTEDYEAR = ?" +
     */
    protected int setCommonData(int i, PerformanceBaseModel model, PreparedStatement ps) throws SQLException {
        i = setFilesAndReturnNewIndex(i, ps, model);
        ps.setString(++i, model.getExternalLink());
        ps.setString(++i, model.getSource());
        ps.setString(++i, model.getSubmitter());
        ps.setString(++i, model.getContent());
        ps.setInt(++i, model.getCheck());
        ps.setString(++i, model.getStatus());
        ps.setString(++i, model.getSelectedYear());
        return i;
    }


    protected <T extends PerformanceBaseModel> void fillInExternalFields(List<T> list) {
        fillInFileInfo(list);
        fillInStuInfo(list);
    }

    private <T extends PerformanceBaseModel> void fillInFileInfo(List<T> list) {
        List<Integer> idsList = list.stream().flatMap(m -> Stream.of(m.getDocumentFileId(), m.getVideoFileId())).filter(Objects::nonNull).collect(Collectors.toList());
        UploadFileDao fileDao = new UploadFileDaoImpl(this);
        Map<Integer, UploadFile> map = fileDao.getByIdsToMap(idsList);
        map.values().forEach(f -> {
            f.set_link(Upload.generateDownloadLink(null, this, f));
        });

        for (PerformanceBaseModel model : list) {
            model.setDocumentFile(map.get(model.getDocumentFileId()));
            model.setVideoFile(map.get(model.getVideoFileId()));
        }
    }

    private <T extends PerformanceBaseModel> void fillInStuInfo(List<T> list) {
        List<Integer> idsList = list.stream().map(PerformanceBaseModel::getRgno).collect(Collectors.toList());
        StudentDao studentDao = new StudentDaoImpl(this);
        Map<Integer, Student> map = new HashMap<>();
        for (Student s : studentDao.getStudentByRgno(idsList)) {
            map.put(s.getRgno(), s);
        }

        for (PerformanceBaseModel model : list) {
            Student stu = map.get(model.getRgno());
            model.setStuBasicInfo(new PerformanceBaseModel.StuBasicInfo(stu.getIdno(), stu.getBirth()));
        }
    }

}
