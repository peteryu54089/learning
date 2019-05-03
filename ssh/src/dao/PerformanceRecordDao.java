package dao;

import model.PerformanceRecord;

import java.sql.SQLException;
import java.util.List;

public interface PerformanceRecordDao {
    List<PerformanceRecord> getPerformanceRecord(String term_year, String term_sem) throws SQLException;
}
