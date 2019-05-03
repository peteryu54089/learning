package dao;

import model.Cadre;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public interface CadreDao {

    void insertCadre(Cadre cadre) throws SQLException;

    void updateCadreByID(Cadre cadre, Integer rgno, Integer id) throws SQLException;

    List<Cadre> getCadre(Integer rgno, String check, String status, String selectYear, Integer page) throws SQLException;

    Integer getPageNumber(Integer rgno) throws SQLException;

    Cadre getCadreByID(Integer rgno, Integer id) throws SQLException;

    void deleteCadre(Integer rgno, Integer id) throws SQLException;

    List<Cadre> getExportCadreRecords(int year, int sem);

    List<Cadre> getExportStuCadreRecords(int year);

    ImportResult importFromExcelInputStream(java.lang.String fileName, InputStream is);

    class ImportResult {
        public List<String> errors = Collections.emptyList();
        public int importRecordCnt = 0;

        public List<String> getErrors() {
            return errors;
        }

        public int getImportRecordCnt() {
            return importRecordCnt;
        }
    }
}
