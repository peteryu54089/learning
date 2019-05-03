package dao;


import model.Licenses;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by David on 2017/7/31.
 */
public interface LicensesDao {
    List<Licenses> getAllLicense() throws SQLException;

    Licenses getLicenseById(int id) throws SQLException;
}
