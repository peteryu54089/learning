package dao;


import model.License;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by David on 2017/8/3.
 */
public interface LicenseRecordDao {
    void insertLicense(License license) throws SQLException;

    void updateLicenseByID(License license, Integer rgno, Integer id) throws SQLException;

    List<License> getLicense(Integer rgno, String check, String status, String selectYear, Integer page) throws SQLException;

    Integer getPageNumber(Integer rgno) throws SQLException;

    License getLicenseByID(Integer rgno, Integer id) throws SQLException;

    void deleteLicense(Integer rgno, Integer id) throws SQLException;

    List<License> getExportStuLicenseRecords(int year);
}
