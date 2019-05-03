package dao;

import model.CUnit;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by David on 2017/8/6.
 */
public interface CUnitDao {
    List<CUnit> getCUnit(Integer year, Integer semester, String clscode, String staff_code, String cunit_name) throws SQLException;
}
