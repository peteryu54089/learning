package dao;

import model.PerformanceSubmitSet;

import java.sql.SQLException;

/**
 * Created by Ching-Yun Yu on 2018/5/31.
 */
public interface PerformanceSubmitSetDao {
    PerformanceSubmitSet getPerformanceSubmitSet() throws SQLException;

    void setPerformanceSubmitSet(PerformanceSubmitSet performanceSubmitSet) throws SQLException;

    void insertPerformanceSubmitSet(PerformanceSubmitSet performanceSubmitSet) throws SQLException;
}
