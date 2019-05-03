package dao.impl;


import dao.PerformanceRecordDao;
import dbconn.DbConn;
import model.HasSchoolSchema;
import model.PerformanceRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PerformanceRecordDaoImpl extends BaseDao implements PerformanceRecordDao {
    protected PerformanceRecordDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
    }

    @Override
    public List<PerformanceRecord> getPerformanceRecord(String term_year, String term_sem) throws SQLException {
        String sql = "SELECT * FROM STUDENT_PERFORMANCE_COVER WHERE term_year LIKE ? AND term_sem LIKE ?";
        List<PerformanceRecord> performanceRecordList = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            int i = 1;
            preparedStatement.setString(i++, "%" + term_year + "%");
            preparedStatement.setString(i++, "%" + term_sem + "%");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                PerformanceRecord performanceRecord = new PerformanceRecord(
                        resultSet.getString("id"),
                        resultSet.getString("term_year"),
                        resultSet.getString("term_sem"),
                        resultSet.getString("booklet")
                );
                performanceRecordList.add(performanceRecord);
            }
            return performanceRecordList;
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
}
