package dao.impl;

import dao.LicensesDao;
import dbconn.DbConn;
import model.HasSchoolSchema;
import model.Licenses;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LicensesDaoImpl extends BaseDao implements LicensesDao {
    public LicensesDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
    }

    @Override
    public List<Licenses> getAllLicense() throws SQLException {
        String sql = "SELECT * FROM LICENSE WHERE 1 = 1";
        List<Licenses> licensesList = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Licenses licenses = new Licenses(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("located"),
                        resultSet.getString("level"),
                        resultSet.getString("type"),
                        resultSet.getString("unit")
                );
                licensesList.add(licenses);
            }
            return licensesList;
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
    public Licenses getLicenseById(int id) throws SQLException {
        String sql = "SELECT * FROM LICENSE WHERE id = ?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Licenses licenses = new Licenses(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("located"),
                        resultSet.getString("level"),
                        resultSet.getString("type"),
                        resultSet.getString("unit")
                );
                return licenses;
            }
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
