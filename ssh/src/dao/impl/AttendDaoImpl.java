package dao.impl;


import dao.AttendDao;
import dbconn.DbConn;
import model.ArCode;
import model.Attend;
import model.HasSchoolSchema;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AttendDaoImpl extends BaseDao implements AttendDao {
    //TODO 頁數整合
    public final static Integer RECORD_PER_PAGE = 10;

    public AttendDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
    }

    @Override
    public List<Attend> getAttend(Integer rgno, String term_year, String term_sem, String ar_code, Integer page) throws SQLException {
        String sql = "SELECT * FROM (\n" +
                "SELECT\n" +
                "ROW_NUMBER()\n" +
                "OVER (\n" +
                "ORDER BY ABS_DATE ) AS RN,Q.*\n" +
                "FROM\n" +
                "(SELECT * FROM STU_ABSENCE) AS Q\n" +
                "WHERE RGNO = ?";
        if (term_year != null) {
            sql += " AND sbj_year=?";
        }
        if (term_sem != null) {
            sql += " AND sbj_sem=?";
        }
        if (ar_code != null) {
            sql += " AND ar_code=?";
        }
        sql += ")";
        sql += "WHERE RN BETWEEN ? AND ?\n";
        List<Attend> attendList = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            int i = 1;
            preparedStatement.setInt(i++, rgno);
            if (term_year != null) {
                preparedStatement.setString(i++, term_year);
            }
            if (term_sem != null) {
                preparedStatement.setString(i++, term_sem);
            }
            if (ar_code != null) {
                preparedStatement.setString(i++, ar_code);
            }
            preparedStatement.setInt(i++, ((page - 1) * RECORD_PER_PAGE) + 1);
            preparedStatement.setInt(i++, ((page) * RECORD_PER_PAGE));
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Attend attend = new Attend(
                        resultSet.getString("rgno"),
                        resultSet.getString("sbj_year"),
                        resultSet.getString("sbj_sem"),
                        resultSet.getTimestamp("abs_Date"),
                        resultSet.getString("ar_code"),
                        resultSet.getString("ar_cname"),
                        resultSet.getInt("period"),
                        resultSet.getString("sprd_name")
                );
                attendList.add(attend);
            }
            return attendList;
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
    public Integer getPageNumber(Integer rgno, String term_year, String term_sem, String ar_code) throws SQLException {
        Integer pagenumber = 1;
        pagenumber = ((getCount(rgno, term_year, term_sem, ar_code) - 1) / RECORD_PER_PAGE) + 1;
        return pagenumber;
    }

    @Override
    public Integer getCount(Integer rgno, String term_year, String term_sem, String ar_code) throws SQLException {
        String sql = "SELECT COUNT(*) FROM STU_ABSENCE WHERE RGNO=?";
        if (term_year != null) {
            sql += " AND sbj_year=?";
        }
        if (term_sem != null) {
            sql += " AND sbj_sem=?";
        }
        if (ar_code != null) {
            sql += " AND ar_code=?";
        }
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int count = 1;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            int i = 1;
            preparedStatement.setInt(i++, rgno);
            if (term_year != null) {
                preparedStatement.setString(i++, term_year);
            }
            if (term_sem != null) {
                preparedStatement.setString(i++, term_sem);
            }
            if (ar_code != null) {
                preparedStatement.setString(i++, ar_code);
            }
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
            return count;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }
        return 1;
    }

    @Override
    public List<Attend> getCalenderByDate(Integer rgno, String start, String end) throws SQLException {
        String sql = "SELECT * FROM STU_ABSENCE WHERE RGNO=? AND ABS_DATE >= ? AND ABS_DATE <=? ORDER BY ABS_DATE,SPERIOD";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            preparedStatement.setInt(1, rgno);
            preparedStatement.setString(2, start);
            preparedStatement.setString(3, end);
            resultSet = preparedStatement.executeQuery();
            List<Attend> attendList = new ArrayList<>();
            while (resultSet.next()) {
                Attend attend = new Attend(
                        resultSet.getString("rgno"),
                        resultSet.getString("sbj_year"),
                        resultSet.getString("sbj_sem"),
                        resultSet.getTimestamp("abs_Date"),
                        resultSet.getString("ar_code"),
                        resultSet.getString("ar_cname"),
                        resultSet.getInt("period"),
                        resultSet.getString("sprd_name")
                );
                attendList.add(attend);
            }
            return attendList;
        }
    }

    @Override
    public List<ArCode> getArCodeList() throws SQLException {
        String sql = "SELECT * FROM STU_AR_LEAVE_ONLY WHERE AR_TYPE IS NULL";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            List<ArCode> arCodeList = new ArrayList<>();
            while (resultSet.next()) {
                ArCode arCode = new ArCode(
                        resultSet.getString("ar_code"),
                        resultSet.getString("ar_cname"),
                        resultSet.getString("ar_use"),
                        resultSet.getString("ar_type")
                );
                arCodeList.add(arCode);
            }
            return arCodeList;
        }
    }
}
