package dao.impl;

import dao.LifeplanDao;
import dbconn.DbConn;
import model.HasSchoolSchema;
import model.LifePlan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LifeplanDaoImpl extends BaseDao implements LifeplanDao {
    public LifeplanDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
    }

    @Override
    public List<LifePlan> getlifeplan(String reg_no) throws SQLException {
        String sql = "SELECT * FROM  LIFEPLAN WHERE reg_no=?";
        ArrayList<LifePlan> lifePlanlist = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                LifePlan lifeplan = new LifePlan(
                        resultSet.getString("register_number"),
                        resultSet.getString("topic"),
                        resultSet.getString("description"),
                        resultSet.getString("file_path")
                );
                lifePlanlist.add(lifeplan);
            }
            return lifePlanlist;
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
    public void uploadlifeplan(LifePlan lifePlan) throws SQLException {
        String sql = "INSERT INTO  LIFEPLAN (reg_no, topic, description, file_path) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            int i = 1;
            preparedStatement.setString(i++, lifePlan.getReg_no());
            preparedStatement.setString(i++, lifePlan.getTopic());
            preparedStatement.setString(i++, lifePlan.getDescription());
            preparedStatement.setString(i++, lifePlan.getFile_path());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
        }
    }

    @Override
    public void deletelifeplan(int id, String reg_no) throws SQLException {
        String sql = "DELETE FROM  LIFEPLAN WHERE id = ? AND reg_no = ?";
        PreparedStatement preparedStatement = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            int i = 1;
            preparedStatement.setInt(i++, id);
            preparedStatement.setString(i++, reg_no);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
        }
    }

}
