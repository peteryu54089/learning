package dao.impl;

import dao.AutobiographyDao;
import dao.UploadFileDao;
import dbconn.DbConn;
import model.Autobiography;
import model.HasSchoolSchema;
import model.UploadFile;
import util.ObjectUtils;

import java.sql.*;
import java.util.*;

public class AutobiographyDaoImpl extends BaseDao implements AutobiographyDao {
    //TODO 頁數整合
    public final static Integer RECORD_PER_PAGE = 2;

    public AutobiographyDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
    }

    @Override
    public void uploadAutobiography(Integer rgno, String topic, String description, Integer main_file_id, Integer sub_file_id, Integer term_year, Integer term_semester) throws SQLException {
        String sql = "INSERT INTO AUTOBIOGRAPHY (RGNO, TOPIC, DESCRIPTION,MAIN_FILE_ID,SUB_FILE_ID,TERM_YEAR,TERM_SEMESTER) VALUES (?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            preparedStatement.setInt(1, rgno);
            preparedStatement.setString(2, topic);
            preparedStatement.setString(3, description);
            preparedStatement.setInt(4, main_file_id);
            if (sub_file_id == null)
                preparedStatement.setNull(5, Types.INTEGER);
            else
                preparedStatement.setInt(5, sub_file_id);
            preparedStatement.setInt(6, term_year);
            preparedStatement.setInt(7, term_semester);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
        }
    }

    @Override
    public void deleteAutobiography(Integer rgno, String ID) throws SQLException {
        String sql = "DELETE FROM AUTOBIOGRAPHY WHERE RGNO = ? AND ID = ?";
        PreparedStatement preparedStatement = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            preparedStatement.setInt(1, rgno);
            preparedStatement.setString(2, ID);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
        }
    }

    @Override
    public Integer getPageNumber(Integer rgno) throws SQLException {
        String sql = "SELECT COUNT(*) FROM AUTOBIOGRAPHY WHERE RGNO=?";
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
    public List<Autobiography> getAutobiography(Integer rgno) throws SQLException {
        String sql = "SELECT * FROM AUTOBIOGRAPHY WHERE RGNO=?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Autobiography> AutobiographyList = new ArrayList<>();
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            preparedStatement.setInt(1, rgno);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Autobiography autobiography = convertToInstance(resultSet);
                AutobiographyList.add(autobiography);
            }
            setUploadFiles(AutobiographyList);
            return AutobiographyList;
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
    public List<Autobiography> getAutobiography(Integer rgno, Integer page) throws SQLException {
        String sql = "SELECT * FROM (\n" +
                "SELECT ROW_NUMBER() OVER (ORDER BY ID) AS RN,AUTOBIOGRAPHY.*\n" +
                "FROM AUTOBIOGRAPHY WHERE RGNO=?)\n" +
                "WHERE RN BETWEEN ? AND ?\n";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Autobiography> AutobiographyList = new ArrayList<>();
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            preparedStatement.setInt(1, rgno);
            preparedStatement.setInt(2, ((page - 1) * RECORD_PER_PAGE) + 1);
            preparedStatement.setInt(3, ((page) * RECORD_PER_PAGE));
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Autobiography autobiography = convertToInstance(resultSet);
                AutobiographyList.add(autobiography);
            }
            setUploadFiles(AutobiographyList);
            return AutobiographyList;
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
    public void updateAutobiographyByID(Integer rgno, String ID, Autobiography autobiography) throws SQLException {
        String sql = "UPDATE AUTOBIOGRAPHY SET TOPIC = ?, DESCRIPTION = ?, MAIN_FILE_ID = ?, SUB_FILE_ID = ? WHERE RGNO = ? AND id = ?";
        PreparedStatement preparedStatement = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            preparedStatement.setString(1, autobiography.getTopic());
            preparedStatement.setString(2, autobiography.getDescription());
            preparedStatement.setInt(3, autobiography.getMain_file_id());
            if(autobiography.getSub_file_id()==null || autobiography.getSub_file_id()==0)
                preparedStatement.setNull(4,Types.INTEGER);
            else
                preparedStatement.setInt(4, autobiography.getSub_file_id());
            preparedStatement.setInt(5, rgno);
            preparedStatement.setString(6, ID);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
        }
    }

    @Override
    public Autobiography getAutobiographyByID(Integer rgno, String ID) throws SQLException {
        String sql = "SELECT * FROM AUTOBIOGRAPHY WHERE ID = ?";
        if (rgno != null)
            sql += "AND RGNO=?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Autobiography autobiography = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            preparedStatement.setString(1, ID);
            if (rgno != null)
                preparedStatement.setInt(2, rgno);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                autobiography = convertToInstance(resultSet);
            }
            setUploadFiles(Collections.singletonList(autobiography));
            return autobiography;
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

    private Autobiography convertToInstance(ResultSet resultSet) throws SQLException {
        return new Autobiography(
                resultSet.getInt("ID"),
                resultSet.getInt("RGNO"),
                resultSet.getString("topic"),
                resultSet.getString("description"),
                resultSet.getInt("main_file_id"),
                resultSet.getInt("sub_file_id"),
                resultSet.getTimestamp("created_at"),
                resultSet.getInt("term_year"),
                resultSet.getInt("term_semester")
        );

    }

    @Override
    public List<Autobiography> getByStuInfo(Integer year, Integer sem, List<Integer> grade) {

        StringBuilder sql = new StringBuilder("SELECT A.*, R.GRADE, I.IDNO\n" +
                "FROM AUTOBIOGRAPHY A\n" +
                "       INNER JOIN STU_RGNO_MAPPING R\n" +
                "                  ON A.REGISTER_NUMBER = R.REG_NO\n" +
                "                    AND A.TERM_SEMESTER = R.TERM_SEM\n" +
                "                    AND A.TERM_YEAR = R.TERM_YEAR\n" +
                "       INNER JOIN STU_CLASS_INFO I ON R.TERM_YEAR = I.TERM_YEAR AND R.TERM_SEM = I.TERM_SEM AND R.REG_NO = I.REG_NO\n" +
                "WHERE  A.DELETED_AT IS NULL\n" +
                "  AND A.TERM_YEAR = ?\n" +
                "  AND A.TERM_SEMESTER = ?\n");
        if (grade.size() > 0) {
            sql.append("  AND R.GRADE IN (?");
            for (int i = 1; i < grade.size(); i++) sql.append(", ?");
            sql.append(")");
        } else {
            return Collections.emptyList();
        }
        try (Connection pcon = DbConn.getConnection(this)) {
            try (PreparedStatement ps = pcon.prepareStatement(sql.toString())) {
                int i = 0;
                ps.setInt(++i, year);
                ps.setInt(++i, sem);
                for (int j = 0; j < grade.size(); j++)
                    ps.setInt(++i, grade.get(j));
                List<Autobiography> ret = new ArrayList<>();
                try (ResultSet resultSet = ps.executeQuery()) {
                    while (resultSet.next()) {
                        Autobiography bio = convertToInstance(resultSet);
                        ObjectUtils.setField(bio, "idno", resultSet.getString("IDNO"));
                        ret.add(bio);
                    }
                }
                setUploadFiles(ret);

                return ret;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }


    private void setUploadFiles(List<Autobiography> list) {
        UploadFileDao uploadFileDao = new UploadFileDaoImpl(this);
        Map<Integer, UploadFile> map =  new HashMap<>();
        List<Integer> ids = new ArrayList<>();
        for (Autobiography crd : list) {
            ids.add(crd.getMain_file_id());
            if(crd.getSub_file_id()!=null && crd.getSub_file_id()!=0)
            {
                ids.add(crd.getSub_file_id());
            }
        }
        for (UploadFile file : uploadFileDao.getByIds(ids)) {
            map.put(file.getId(),file);
        }
        for(Autobiography crd : list)
        {
            crd.setMain_uploadFile(map.get(crd.getMain_file_id()));
            crd.setSub_uploadFile(map.get(crd.getSub_file_id()));
        }
    }
}