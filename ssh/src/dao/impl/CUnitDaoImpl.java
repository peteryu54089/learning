package dao.impl;

import dao.CUnitDao;
import dbconn.DbConn;
import model.CUnit;
import model.HasSchoolSchema;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 2018/1/29.
 */
public class CUnitDaoImpl extends BaseDao implements CUnitDao {

    public CUnitDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
    }

    @Override
    public List<CUnit> getCUnit(Integer year, Integer semester, String cls_code, String staff_code, String cunit_name) throws SQLException {
        String sql = "SELECT DISTINCT TERM_YEAR, TERM_SEM, CLS_CODE, CLS_CNAME " +
                (staff_code == null ? ", NULL AS STAFF_CODE FROM STU_CLASS_INFO S\n" : ", STAFF_CODE FROM STU_CLASS_INFO_WITH_TUTOR S\n") +
                "WHERE 1 = 1";
        if (year != null) {
            sql += " AND TERM_YEAR=?";
        }
        if (semester != null) {
            sql += " AND TERM_SEM=?";
        }
        if (cls_code != null) {
            sql += " AND CLS_CODE=?";
        }
        if (staff_code != null) {
            sql += " AND STAFF_CODE=?";
        }
        if (cunit_name != null) {
            sql += " AND cunit_cname=?";
        }
        List<CUnit> CUnitList = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            int i = 1;
            if (year != null) {
                preparedStatement.setInt(i++, year);
            }
            if (semester != null) {
                preparedStatement.setInt(i++, semester);
            }
            if (cls_code != null) {
                preparedStatement.setString(i++, cls_code);
            }
            if (staff_code != null) {
                preparedStatement.setString(i++, staff_code);
            }
            if (cunit_name != null) {
                preparedStatement.setString(i++, cunit_name);
            }
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                CUnit cUnit = new CUnit(
                        resultSet.getInt("TERM_YEAR"),
                        resultSet.getInt("TERM_SEM"),
                        resultSet.getString("CLS_CODE"),
                        resultSet.getString("STAFF_CODE"),
                        resultSet.getString("CLS_CNAME")
                );
                CUnitList.add(cUnit);
            }
            return CUnitList;
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
