package dao;


import model.CourseRecordDocument;

import java.sql.SQLException;
import java.util.List;

public interface CourseRecordDocumentDao {

    void insertCourseRecordDocument(CourseRecordDocument courseRecordDocument) throws SQLException;

    List<CourseRecordDocument> getCourseRecordDocument(String crid) throws SQLException;

    List<CourseRecordDocument> getCourseRecordDocumentWithIdno(String crid) throws SQLException;

    List<CourseRecordDocument> getCourseRecordDocuments(List<String> crid) throws SQLException;

    CourseRecordDocument getCourseRecordDocumentByID(String ID) throws SQLException;
}

