package dao;

import model.role.PerformanceSubmitter;

import java.sql.SQLException;

/**
 * Created by Ching Yun Yu on 2018/4/27.
 */
public interface PerformanceSubmitterDao extends RoleDao {
    PerformanceSubmitter getPerformanceSubmitterByRegNumber(String regNumber) throws SQLException;
}
