package dao.impl;

import dao.CadreDao;
import dao.StudentDao;
import dbconn.DbConn;
import model.Cadre;
import model.HasSchoolSchema;
import org.apache.poi.ss.usermodel.CellType;
import util.DbUtils;
import util.excel.ExcelParse;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CadreDaoImpl extends BasePerformanceDao implements CadreDao {
    //TODO 頁數整合
    public final static Integer RECORD_PER_PAGE = 4;

    public CadreDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
        // Default Constructor
    }

    private String getInsertSQL() {
        return "INSERT INTO CADRE_RECORD (" +
                "RGNO, " +
                "CONTENT, SOURCE, " +
                "unit, " +
                "startTime, endTime, " +
                "term, job, level, " +
                "DOCUMENT_FILE_ID, VIDEO_FILE_ID, EXTERNAL_LINK" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    public void insertCadre(Cadre cadre) throws SQLException {
        String sql = getInsertSQL();
        try (Connection pcon = DbConn.getConnection(this)) {
            try (PreparedStatement ps = pcon.prepareStatement(sql)) {
                setCadreParams(cadre, ps);
                ps.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setCadreParams(Cadre cadre, PreparedStatement ps) throws SQLException {
        int i = 0;
        ps.setInt(++i, cadre.getRgno());
        ps.setString(++i, cadre.getContent());
        ps.setString(++i, cadre.getSource());
        ps.setString(++i, cadre.getUnit());
        ps.setString(++i, cadre.getStartTime());
        ps.setString(++i, cadre.getEndTime());
        ps.setString(++i, cadre.getTerm());
        ps.setString(++i, cadre.getJob());
        ps.setString(++i, cadre.getLevel());
        i = setFilesAndReturnNewIndex(i, ps, cadre);
        ps.setString(++i, cadre.getExternalLink());
    }

    @Override
    public void updateCadreByID(Cadre cadre, Integer rgno, Integer id) throws SQLException {
        String sql = "UPDATE CADRE_RECORD SET " +
                "UNIT = ?, " +
                "STARTTIME = ?, ENDTIME = ?, " +
                "TERM = ?, " +
                "JOB=?, LEVEL = ?, " +
                "DOCUMENT_FILE_ID = ?,VIDEO_FILE_ID = ?,EXTERNAL_LINK= ?," +
                "SOURCE = ?, SUBMITTER = ?, CONTENT = ? ,CHECK = ? , STATUS = ?,SELECTEDYEAR = ?" +
                "  WHERE RGNO = ? AND id = ?";

        Cadre oldModel = getCadreByID(rgno, id);
        deleteOldUploadedFile(oldModel, cadre);
        try (Connection pcon = DbConn.getConnection(this); PreparedStatement ps = pcon.prepareStatement(sql);) {
            int i = 0;

            ps.setString(++i, cadre.getUnit());
            ps.setString(++i, cadre.getStartTime());
            ps.setString(++i, cadre.getEndTime());
            ps.setString(++i, cadre.getTerm());
            ps.setString(++i, cadre.getJob());
            ps.setString(++i, cadre.getLevel());
            i = setCommonData(i, cadre, ps);

            ps.setInt(++i, rgno);
            ps.setInt(++i, id);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Integer getPageNumber(Integer rgno) throws SQLException {
        String sql = "SELECT COUNT(*) FROM CADRE_RECORD WHERE RGNO=?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int pagenumber = 1;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            preparedStatement.setInt(1, rgno);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                pagenumber = resultSet.getInt(1);
                pagenumber = ((pagenumber - 1) / RECORD_PER_PAGE) + 1;
            }
            return pagenumber;
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
    public List<Cadre> getCadre(Integer rgno, String check, String status, String selectYear, Integer page) throws SQLException {
        String sql = "SELECT * FROM (\n" +
                "SELECT ROW_NUMBER() OVER (ORDER BY ID) AS RN,CADRE_RECORD.*\n" +
                "FROM CADRE_RECORD WHERE 1=1 ";
        if (rgno != null)
            sql += " AND RGNO=?";
        if (status != null)
            sql += " AND status = ?";
        if (check != null)
            sql += " AND check = ?";
        if (selectYear != null)
            sql += " AND SELECTEDYEAR = ?";
        sql += ")";
        if (page != null)
            sql += "WHERE RN BETWEEN ? AND ?\n";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Cadre> CadreList = new ArrayList<>();
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            Integer i = 1;
            if (rgno != null)
                preparedStatement.setInt(i++, rgno);
            if (status != null)
                preparedStatement.setString(i++, status);
            if (check != null)
                preparedStatement.setString(i++, check);
            if (selectYear != null)
                preparedStatement.setString(i++, selectYear);
            if (page != null) {
                preparedStatement.setInt(i++, ((page - 1) * RECORD_PER_PAGE) + 1);
                preparedStatement.setInt(i++, ((page) * RECORD_PER_PAGE));
            }
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Cadre cadre = convertToInstance(resultSet);
                CadreList.add(cadre);
            }
            fillInExternalFields(CadreList);
            return CadreList;
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
    public Cadre getCadreByID(Integer rgno, Integer id) throws SQLException {
        String sql = "SELECT * FROM CADRE_RECORD WHERE ID = ?";
        if (rgno != null)
            sql += "AND RGNO=?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Cadre cadre = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            if (rgno != null)
                preparedStatement.setInt(2, rgno);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                cadre = convertToInstance(resultSet);
            }

            fillInExternalFields(Collections.singletonList(cadre));
            return cadre;
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
    public void deleteCadre(Integer rgno, Integer id) throws SQLException {
        Cadre cadre = getCadreByID(rgno, id);
        deleteOldUploadedFile(cadre, null);
        String sql = "DELETE FROM CADRE_RECORD WHERE RGNO = ? AND ID = ?";
        try (Connection pcon = DbConn.getConnection(this);
             PreparedStatement ps = pcon.prepareStatement(sql)) {
            int i = 0;
            ps.setInt(++i, rgno);
            ps.setInt(++i, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Cadre> getExportCadreRecords(int year, int sem) {
        final List<Cadre> list = new ArrayList<>();
        try {
            try (Connection pcon = DbConn.getConnection(this)) {
                try (PreparedStatement ps = pcon.prepareStatement("SELECT * " +
                        "FROM CADRE_RECORD " +
                        "WHERE TERM = ? AND STATUS = ? AND SOURCE = ? " +
                        "ORDER BY TERM, RGNO, ID")) {
                    int i = 0;
                    // ps.setString(++i, regNo);
                    ps.setString(++i, String.format("%3d%d", year, sem));
                    ps.setString(++i, "2"); // 已勾選
                    ps.setString(++i, Cadre.SourceType.CADRE_SUBMITTER);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            list.add(convertToInstance(rs));
                        }
                    }
                    fillInExternalFields(list);
                }
            }
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }

        return list;
    }

    private static Cadre convertToInstance(ResultSet resultSet) throws SQLException {
        Cadre cadre = new Cadre(
                resultSet.getInt("ID"),
                resultSet.getInt("RGNO"),
                resultSet.getString("CONTENT"),
                resultSet.getString("SOURCE"),
                resultSet.getString("unit"),
                resultSet.getString("starttime"),
                resultSet.getString("endtime"),
                resultSet.getString("term"),
                resultSet.getString("job"),
                resultSet.getString("level"),
                DbUtils.getInteger(resultSet, "DOCUMENT_FILE_ID"),
                DbUtils.getInteger(resultSet, "VIDEO_FILE_ID"),
                resultSet.getString("EXTERNAL_LINK"),
                resultSet.getString("SUBMITTER"),
                resultSet.getString("SELECTEDYEAR"),
                resultSet.getInt("CHECK"),
                resultSet.getString("STATUS"),
                resultSet.getTimestamp("CREATED_AT")
        );
        cadre.setSelectedYear(resultSet.getString("SELECTEDYEAR"));
        cadre.setCreated_at(resultSet.getTimestamp("created_at"));
        return cadre;
    }


    @Override
    public List<Cadre> getExportStuCadreRecords(int year) {
        final List<Cadre> list = new ArrayList<>();
        try {
            try (Connection pcon = DbConn.getConnection(this)) {
                try (PreparedStatement ps = pcon.prepareStatement("SELECT * " +
                        "FROM CADRE_RECORD " +
                        "WHERE SELECTEDYEAR LIKE ? AND STATUS = ? AND SOURCE = ? " +
                        "ORDER BY TERM, RGNO, ID")) {
                    int i = 0;
                    // ps.setString(++i, regNo);
                    ps.setString(++i, String.format("%3d", year));
                    ps.setString(++i, "2"); // 已勾選
                    ps.setString(++i, Cadre.SourceType.STUDENT);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            list.add(convertToInstance(rs));
                        }
                        fillInExternalFields(list);
                    }
                }
            }
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public ImportResult importFromExcelInputStream(String fileName, InputStream is) {
        List<String> errors = new ArrayList<>();
        List<Cadre> cadres = new ArrayList<>();
        final ImportResult result = new ImportResult();
        result.errors = errors;

        try (ExcelParse excelParse = new ExcelParse()) {
            excelParse.loadExcel(fileName, is);
            if (!doCheck(excelParse, errors, cadres)) return result;

            try (Connection pcon = DbConn.getConnection(this)) {
                boolean oldAutoCommit = pcon.getAutoCommit();
                pcon.setAutoCommit(false);
                try (PreparedStatement ps = pcon.prepareStatement(getInsertSQL())) {
                    for (Cadre c : cadres) {
                        setCadreParams(c, ps);
                        ps.addBatch();
                    }

                    ps.executeBatch();
                    pcon.commit();
                } finally {
                    pcon.setAutoCommit(oldAutoCommit);
                }

                if (errors.size() == 0) {
                    result.importRecordCnt = cadres.size();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            errors.add("系統發生未知錯誤: " + e.getMessage());
        }

        return result;
    }

    private boolean doCheck(ExcelParse excelParse, List<String> errors, List<Cadre> cadres) {
        try {
            int sheetNo = 1;
            int rowCnt = excelParse.getRowCount(sheetNo);

            for (int row = 2; row <= rowCnt; row++) {
                final String rowPrefix = "第" + row + "行: ";

                for (int cellNo = 1; cellNo <= 8; cellNo++)
                    excelParse.getExcelCellByRowAndCell(sheetNo, row, cellNo).setCellType(CellType.STRING);
                int cellNo = 0;
                String year = excelParse.getExcelCellByRowAndCell(sheetNo, row, ++cellNo).getStringCellValue().trim();
                String sem = excelParse.getExcelCellByRowAndCell(sheetNo, row, ++cellNo).getStringCellValue().trim();
                String regNo = excelParse.getExcelCellByRowAndCell(sheetNo, row, ++cellNo).getStringCellValue().trim();
                String unit = excelParse.getExcelCellByRowAndCell(sheetNo, row, ++cellNo).getStringCellValue().trim();
                String startDate = excelParse.getExcelCellByRowAndCell(sheetNo, row, ++cellNo).getStringCellValue().trim();
                String endDate = excelParse.getExcelCellByRowAndCell(sheetNo, row, ++cellNo).getStringCellValue().trim();
                String position = excelParse.getExcelCellByRowAndCell(sheetNo, row, ++cellNo).getStringCellValue().trim();
                String level = excelParse.getExcelCellByRowAndCell(sheetNo, row, ++cellNo).getStringCellValue().trim();

                try {
                    Integer.parseInt(year);
                } catch (NumberFormatException ignored) {
                    errors.add(rowPrefix + "學年輸入錯誤");
                }

                if (!sem.equals("1") && !sem.equals("2")) {
                    errors.add(rowPrefix + "學期輸入錯誤");
                }

                if (startDate.length() != 7) {
                    errors.add(rowPrefix + "開始日期輸入錯誤");
                }

                if (endDate.length() != 7) {
                    errors.add(rowPrefix + "結束日期輸入錯誤");
                }

                try {
                    int levelInt = Integer.parseInt(level);
                    if (levelInt > 3 || levelInt < 1) {
                        errors.add(rowPrefix + "等級輸入錯誤，只能為1~3");
                    }
                } catch (Exception ignored) {
                    errors.add(rowPrefix + "幹部等級輸入錯誤");
                }

                //
                StudentDao studentDao = new StudentDaoImpl(this);
                Integer rgno = studentDao.getRgnoFromRegNo(year, sem, regNo);
                if (rgno == null) {
                    errors.add(rowPrefix + String.format("在%s-%s學期找不到學號為\"%s\"的學生", year, sem, regNo));
                } else if (0 == errors.size()) {
                    cadres.add(new Cadre(
                            rgno,
                            String.format("學校匯入 學期:%s-%s 幹部", year, sem),
                            Cadre.SourceType.CADRE_SUBMITTER,
                            unit, startDate, endDate,
                            year + sem,
                            position, level,
                            null, null, ""));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0 == errors.size();
    }
}
