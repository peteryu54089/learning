package dao.impl;

import dao.GroupCounsel3Dao;
import dbconn.DbConn;
import model.GroupCounsel3;
import model.HasSchoolSchema;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ching Yun Yu on 2018/5/5.
 */
public class GroupCounsel3DaoImpl extends BaseDao implements GroupCounsel3Dao {
    public GroupCounsel3DaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
        // Default Constructor
    }

    @Override
    public List<GroupCounsel3> getGroupCounsel3ByText1AndText2(String text1, String text2) throws SQLException {
        String tableName = "GROUP_COUNSEL3";

        String sql = "SELECT * FROM GROUP_COUNSEL3 WHERE text1 LIKE ? AND text2 LIKE ?";
        List<GroupCounsel3> groupCounsel3List = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            DatabaseMetaData dmd = pcon.getMetaData();
            try (ResultSet rs = dmd.getTables(null, null, tableName, null)) {
                try (Statement stmt = pcon.createStatement()) {
                    if (!rs.next()) {
                        stmt.executeUpdate("CREATE TABLE GROUP_COUNSEL3" + "(text1 VARCHAR(30), text2 VARCHAR(30), label1 VARCHAR(30), label2 VARCHAR(30), label3 VARCHAR(30), label4 VARCHAR(30), label5 VARCHAR(30), label6 VARCHAR(30))");
                        //stmt.executeUpdate("alter table GROUP_COUNSEL3 add primary key(text1, text2, label1, label2, label3, label4, label5);");
                    }
                }
            }

            preparedStatement = pcon.prepareStatement(sql);
            int i = 1;
            preparedStatement.setString(i++, "%" + text1 + "%");
            preparedStatement.setString(i++, "%" + text2 + "%");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                GroupCounsel3 groupCounsel3 = new GroupCounsel3(
                        resultSet.getString("text1"),
                        resultSet.getString("text2"),
                        resultSet.getString("label1"),
                        resultSet.getString("label2"),
                        resultSet.getString("label3"),
                        resultSet.getString("label4"),
                        resultSet.getString("label5"),
                        resultSet.getString("label6")
                );
                groupCounsel3List.add(groupCounsel3);
            }
            return groupCounsel3List;
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
    public void deleteGroupCounsel3(String text1, String text2, String label1, String label2, String label3, String label4, String label5, String label6) throws SQLException {
        String sql = "DELETE FROM GROUP_COUNSEL3 WHERE text1 = ? And text2 = ? And label1 = ? And label2 = ? And label3 = ? And label4 = ? And label5 = ? And label6 = ?";
        PreparedStatement preparedStatement = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            int i = 1;
            preparedStatement.setString(i++, text1);
            preparedStatement.setString(i++, text2);
            preparedStatement.setString(i++, label1);
            preparedStatement.setString(i++, label2);
            preparedStatement.setString(i++, label3);
            preparedStatement.setString(i++, label4);
            preparedStatement.setString(i++, label5);
            preparedStatement.setString(i++, label6);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
        }
    }

}
