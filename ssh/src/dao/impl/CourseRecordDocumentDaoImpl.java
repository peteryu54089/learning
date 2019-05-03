package dao.impl;

import dao.CourseRecordDocumentDao;
import dao.UploadFileDao;
import dbconn.DbConn;
import model.CourseRecord;
import model.CourseRecordDocument;
import model.HasSchoolSchema;
import model.UploadFile;
import util.ObjectUtils;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class CourseRecordDocumentDaoImpl extends BaseDao implements CourseRecordDocumentDao {
    public CourseRecordDocumentDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
    }

    @Override
    public void insertCourseRecordDocument(CourseRecordDocument courseRecordDocument) throws SQLException {
        String sql = "INSERT INTO STUDENT_COURSE_RECORD_DOCUMENT_RELATION (CRID, UPLOAD_FILE_ID) VALUES (?,?)";
        PreparedStatement preparedStatement = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            int i = 1;
            preparedStatement.setInt(i++, courseRecordDocument.getCrid());
            preparedStatement.setInt(i++, courseRecordDocument.getUpload_file_id());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
        }
    }

    @Override
    public List<CourseRecordDocument> getCourseRecordDocument(String crid) throws SQLException {
        String sql = "SELECT * FROM STUDENT_COURSE_RECORD_DOCUMENT WHERE CRID=? AND DELETED_AT IS NULL";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<CourseRecordDocument> courseRecordDocumentList = new ArrayList<>();
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            preparedStatement.setString(1, crid);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                CourseRecordDocument courseRecordDocument = new CourseRecordDocument(
                        resultSet.getInt("CRID"),
                        resultSet.getInt("UPLOAD_FILE_ID"),
                        resultSet.getString("DOCUMENT"),
                        resultSet.getString("ORIGINAL_FILENAME"),
                        resultSet.getTimestamp("DELETED_AT")
                );
                courseRecordDocumentList.add(courseRecordDocument);
            }
            setUploadFiles(courseRecordDocumentList);
            return courseRecordDocumentList;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }
        return null;
    }

    @Override
    public List<CourseRecordDocument> getCourseRecordDocumentWithIdno(String crid) throws SQLException {
        String sql =
                "SELECT d.*, R.IDNO " +
                        "FROM STUDENT_COURSE_RECORD_DOCUMENT d " +
                        "INNER JOIN STUDENT_COURSE_RECORD SCR " +
                        "   on d.CRID = SCR.ID " +
                        "INNER JOIN STU_RGNO_MAPPING R " +
                        "   ON SCR.TERM_YEAR = R.TERM_YEAR AND SCR.TERM_SEM = R.TERM_SEM AND SCR.REGISTER_NUMBER = R.REG_NO " +
                        "WHERE d.CRID=? AND d.DELETED_AT IS NULL ";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<CourseRecordDocument> courseRecordDocumentList = new ArrayList<>();
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            preparedStatement.setString(1, crid);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                CourseRecordDocument courseRecordDocument = new CourseRecordDocument(
                        resultSet.getInt("CRID"),
                        resultSet.getInt("UPLOAD_FILE_ID"),
                        resultSet.getString("DOCUMENT"),
                        resultSet.getString("ORIGINAL_FILENAME"),
                        resultSet.getTimestamp("DELETED_AT")
                );
                ObjectUtils.setField(courseRecordDocument, "idno", resultSet.getString("IDNO"));
                courseRecordDocumentList.add(courseRecordDocument);
            }
            setUploadFiles(courseRecordDocumentList);
            return courseRecordDocumentList;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }
        return null;
    }

    @Override
    public List<CourseRecordDocument> getCourseRecordDocuments(List<String> crid) throws SQLException {
        if (crid == null || crid.size() == 0)
            return Collections.emptyList();

        String sql = "SELECT * FROM STUDENT_COURSE_RECORD_DOCUMENT WHERE CRID IN (?";
        int len = crid.size();
        for (int i = 1; i < len; i++) {
            sql += ", ?";
        }
        sql += " ) AND DELETED_AT IS NULL";

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<CourseRecordDocument> courseRecordDocumentList = new ArrayList<>();
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            int i = 0;
            for (int j = 0; j < len; j++) {
                preparedStatement.setString(++i, crid.get(j));
            }

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                CourseRecordDocument courseRecordDocument = new CourseRecordDocument(
                        resultSet.getInt("CRID"),
                        resultSet.getInt("UPLOAD_FILE_ID"),
                        resultSet.getString("DOCUMENT"),
                        resultSet.getString("ORIGINAL_FILENAME"),
                        resultSet.getTimestamp("DELETED_AT")
                );
                courseRecordDocumentList.add(courseRecordDocument);
            }
            setUploadFiles(courseRecordDocumentList);
            return courseRecordDocumentList;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }
        return null;
    }

    @Override
    public CourseRecordDocument getCourseRecordDocumentByID(String ID) throws SQLException {
        String sql = "SELECT * FROM STUDENT_COURSE_RECORD_DOCUMENT WHERE ID = ?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        CourseRecordDocument courseRecordDocument = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            preparedStatement.setString(1, ID);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                courseRecordDocument = new CourseRecordDocument(
                        resultSet.getInt("crid"),
                        resultSet.getInt("UPLOAD_FILE_ID"),
                        resultSet.getString("DOCUMENT"),
                        resultSet.getString("ORIGINAL_FILENAME"),
                        resultSet.getTimestamp("DELETED_AT")
                );
            }
            setUploadFiles(Collections.singletonList(courseRecordDocument));
            return courseRecordDocument;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }
        return null;
    }

    private void setUploadFiles(List<CourseRecordDocument> list){
        Map<Integer, CourseRecordDocument> map = new HashMap<>();
        for(CourseRecordDocument crd : list){
            map.put(crd.getUpload_file_id(), crd);
        }
        List<Integer> ids = new ArrayList<>(map.keySet());
        UploadFileDao fileDao = new UploadFileDaoImpl(this);
        List<UploadFile> files = fileDao.getByIds(ids);
        for(UploadFile file : files){
            map.get(file.getId()).setUploadFile(file);
        }
    }
}
