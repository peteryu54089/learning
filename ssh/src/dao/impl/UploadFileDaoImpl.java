package dao.impl;

import dao.UploadFileDao;
import dbconn.DbConn;
import model.HasSchoolSchema;
import model.Semester;
import model.UploadFile;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class UploadFileDaoImpl extends BaseDao implements UploadFileDao {
    private static String webInfoPath = null;

    public UploadFileDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
    }

    @Override
    public UploadFile insert(String baseDir, String schoolId, Semester sem, Integer rgno, String originalFilename, InputStream is) {
        return insert(baseDir, schoolId, sem, rgno, null, originalFilename, is);
    }

    @Override
    public UploadFile insert(String baseDir, String schoolId, Semester sem, String staffCode, String originalFilename, InputStream is) {
        return insert(baseDir, schoolId, sem, null, staffCode, originalFilename, is);
    }

    @Override
    public UploadFile getById(int id) {
        String sql = "SELECT * FROM UPLOAD_FILES WHERE ID = ?";
        try (Connection con = DbConn.getConnection(this)) {
            try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                int i = 0;
                ps.setInt(++i, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return convertToInstance(rs);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<UploadFile> getByIds(List<Integer> ids) {
        String sql = "SELECT * FROM UPLOAD_FILES WHERE ";
        if (ids == null || ids.size() == 0) return Collections.emptyList();
        StringBuilder sb = new StringBuilder();
        sb.append("ID IN (?");
        for (int i = 1, j = ids.size(); i < j; i++) {
            sb.append(", ?");
        }
        sb.append(")");

        sb.insert(0, sql);
        try (Connection con = DbConn.getConnection(this)) {
            try (PreparedStatement ps = con.prepareStatement(sb.toString(), Statement.RETURN_GENERATED_KEYS)) {
                int i = 0;
                for (Integer id : ids) {
                    ps.setInt(++i, id);
                }

                try (ResultSet rs = ps.executeQuery()) {
                    List<UploadFile> ret = new ArrayList<>();
                    while (rs.next()) {
                        ret.add(convertToInstance(rs));
                    }

                    return ret;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean deleteById(Integer id) {
        if(id==null) return true;
        checkWebInfNull();
        UploadFile uploadFile = getById(id);
        if (uploadFile == null) return false;
        Path filePath = Paths.get(webInfoPath, uploadFile.getFilePath());
        if (filePath.toFile().delete()) {
            // String sql = "UPDATE UPLOAD_FILES SET DELETED_AT = CURRENT_TIMESTAMP WHERE ID = ?";
            String sql = "DELETE FROM UPLOAD_FILES WHERE ID = ?";
            try (Connection con = DbConn.getConnection(this)) {
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    int i = 0;
                    ps.setInt(++i, id);
                    ps.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private UploadFile insert(
            final String baseDir, String schoolId,
            final Semester sem,
            Integer rgno, String staffCode,
            String originalFilename,
            InputStream is
    ) {
        checkWebInfNull();
        String randomFilename = UUID.randomUUID().toString();
        String sql = "INSERT INTO UPLOAD_FILES (" +
                "RGNO, STAFF_CODE, FILE_PATH, FILE_NAME, TERM_YEAR, TERM_SEM" +
                ") VALUES (?, ?, ?, ?, ?, ?)";

        if ((rgno == null && staffCode == null) || (rgno != null && staffCode != null)) {
            throw new UnsupportedOperationException();
        }

        Path filePath = Paths.get(
                baseDir, schoolId, String.valueOf(sem.year),
                rgno != null ? String.valueOf(rgno) : staffCode,
                randomFilename
        );

        try (Connection con = DbConn.getConnection(this)) {
            try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                int i = 0;
                if (rgno == null) {
                    ps.setNull(++i, Types.INTEGER);
                } else {
                    ps.setInt(++i, rgno);
                }
                final String replacedPath = filePath.toString().replaceAll(Pattern.quote("\\"), "/");
                ps.setString(++i, staffCode);
                ps.setString(++i, replacedPath);
                ps.setString(++i, originalFilename);
                ps.setInt(++i, sem.year);
                ps.setInt(++i, sem.semester);

                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        Path target = Paths.get(webInfoPath, replacedPath);
                        target.toFile().getParentFile().mkdirs();
                        Files.write(target, IOUtils.toByteArray(is));

                        return new UploadFile(
                                id, rgno, staffCode,
                                replacedPath, originalFilename,
                                new Timestamp(new java.util.Date().getTime()),
                                sem.year, sem.semester);
                    }
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static UploadFile convertToInstance(ResultSet rs) throws SQLException {
        return new UploadFile(
                rs.getInt("ID"),
                rs.getInt("RGNO"),
                rs.getString("STAFF_CODE"),
                rs.getString("FILE_PATH"),
                rs.getString("FILE_NAME"),
                rs.getTimestamp("CREATED_AT"),
                rs.getInt("TERM_YEAR"),
                rs.getInt("TERM_SEM")
        );
    }

    private static void checkWebInfNull() {
        if (webInfoPath == null)
            throw new NullPointerException("webInfoPath not set");
    }

    public static void setWebInfPath(String webInfoPath) {
        UploadFileDaoImpl.webInfoPath = webInfoPath;
    }

    public static String getWebInfoPath() {
        checkWebInfNull();
        return UploadFileDaoImpl.webInfoPath;
    }
}
