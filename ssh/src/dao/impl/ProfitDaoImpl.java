package dao.impl;

import dao.ProfitDao;
import dbconn.DbConn;
import model.CourseRecord;
import model.HasSchoolSchema;
import model.Profit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by David on 2018/1/29.
 */
public class ProfitDaoImpl extends BaseDao implements ProfitDao {

    public ProfitDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
    }
    @Override
    public List<Profit> getProfit(int year, int sem, String staffCode) {
        String sql = "SELECT A.* " +
                "FROM STU_COURSE_RECORD_WITH_DETAILS A " +
                "INNER JOIN TEA_TEACHING_COURSE B ON A.COURSE_NUM = B.COURSE_NUM " +
                "WHERE A.TERM_YEAR = ? AND A.TERM_SEM = ? " + 
                "AND B.STAFF_CODE = ? " +
                "AND STATUS = ? " +
                "ORDER BY A.COURSE_NUM, CREATED_AT DESC";
        List<Profit> profitList = new ArrayList<>();
        try (Connection pcon = DbConn.getConnection(this)) {
            try (PreparedStatement preparedStatement = pcon.prepareStatement(sql)) {
                int i = 0;
                preparedStatement.setInt(++i, year);
                preparedStatement.setInt(++i, sem);
                preparedStatement.setString(++i, staffCode);
                preparedStatement.setString(++i, CourseRecord.CourseStatus.未認證);
                try (ResultSet rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        Profit profit = new Profit(
                                rs.getInt("ID"),
                                rs.getString("REG_NO"),
                                rs.getInt("TERM_YEAR"),
                                rs.getString("TERM_SEM"),
                                rs.getInt("COURSE_NUM"),
                                rs.getString("COURSE_CNAME"),
                                rs.getString("REG_NO"),
                                rs.getString("CNAME")
                        );
                        profitList.add(profit);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

        return profitList;
    }
}
