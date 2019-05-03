package dao;

import model.Final;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by David on 2017/10/13.
 */
public interface FinalDao {
    int getPerformanceCount(String id_no) throws SQLException;

    int getCourseCount(String id_no) throws SQLException;

    boolean getByIdAndType(int id, String type, String idno) throws SQLException;

    void insertToFinal(int id, String type, String idno) throws SQLException;

    List<Final> getFinalByIdno(String idno) throws SQLException;

    void deleteFinalByidAndIdno(int tid, String type, String idno) throws SQLException;

    List<Final> getAllByType(String type) throws SQLException;
}
