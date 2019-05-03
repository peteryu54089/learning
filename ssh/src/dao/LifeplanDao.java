package dao;

import model.LifePlan;

import java.sql.SQLException;
import java.util.List;

public interface LifeplanDao {
    List<LifePlan> getlifeplan(String reg_no) throws SQLException;

    void uploadlifeplan(LifePlan lifePlan) throws SQLException;

    void deletelifeplan(int id, String reg_no) throws SQLException;

}
