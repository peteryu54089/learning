package dao.impl;

import dao.StudyPlanDao;
import dao.UploadFileDao;
import dbconn.DbConn;
import model.Autobiography;
import model.HasSchoolSchema;
import model.StudyPlan;
import model.UploadFile;
import util.ObjectUtils;

import java.sql.*;
import java.util.*;

public class StudyPlanDaoImpl extends BaseDao implements StudyPlanDao {
    //TODO 頁數整合
    //TODO 改回原TABLE名稱
    public final static Integer RECORD_PER_PAGE = 2;

    public StudyPlanDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
    }

    @Override
    public void uploadStudyPlan(Integer rgno, String topic, String description, Integer main_file_id, Integer sub_file_id, Integer term_year, Integer term_semester) throws SQLException {
        String sql = "INSERT INTO STUDY_PLAN (RGNO, TOPIC, DESCRIPTION,MAIN_FILE_ID,SUB_FILE_ID,TERM_YEAR,TERM_SEMESTER) VALUES (?,?,?,?,?,?,?)";
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
    public void deleteStudyPlan(Integer rgno, String ID) throws SQLException {
        String sql = "DELETE FROM STUDY_PLAN WHERE RGNO = ? AND ID = ?";
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
        String sql = "SELECT COUNT(*) FROM STUDY_PLAN WHERE RGNO =?";
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
    public List<StudyPlan> getStudyPlan(Integer rgno) throws SQLException {
        String sql = "SELECT * FROM STUDY_PLAN WHERE RGNO=?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<StudyPlan> StudyPlanList = new ArrayList<>();
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            preparedStatement.setInt(1, rgno);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                StudyPlan studyPlan =convertToInstance (resultSet);
                StudyPlanList.add(studyPlan);
            }
            setUploadFiles(StudyPlanList);
            return StudyPlanList;
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
    public List<StudyPlan> getStudyPlan(Integer rgno, Integer page) throws SQLException {
        String sql = "SELECT * FROM (\n" +
                "SELECT ROW_NUMBER() OVER (ORDER BY ID) AS RN,STUDY_PLAN.*\n" +
                "FROM STUDY_PLAN WHERE RGNO=? )\n" +
                "WHERE RN BETWEEN ? AND ?\n";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<StudyPlan> StudyPlanList = new ArrayList<>();
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            preparedStatement.setInt(1, rgno);
            preparedStatement.setInt(2, ((page - 1) * RECORD_PER_PAGE) + 1);
            preparedStatement.setInt(3, ((page) * RECORD_PER_PAGE));
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                StudyPlan studyPlan = convertToInstance(resultSet);
                StudyPlanList.add(studyPlan);
            }
            setUploadFiles(StudyPlanList);
            return StudyPlanList;
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
    public void updateStudyPlanByID(Integer rgno, String ID, StudyPlan studyPlan) throws SQLException {
        String sql = "UPDATE STUDY_PLAN SET TOPIC = ?, DESCRIPTION = ?, MAIN_FILE_ID = ?, SUB_FILE_ID = ? WHERE RGNO = ? AND id = ?";
        PreparedStatement preparedStatement = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            preparedStatement.setString(1, studyPlan.getTopic());
            preparedStatement.setString(2, studyPlan.getDescription());
            preparedStatement.setInt(3, studyPlan.getMain_file_id());
            if (studyPlan.getSub_file_id() == null || studyPlan.getSub_file_id() == 0)
                preparedStatement.setNull(4, Types.INTEGER);
            else
                preparedStatement.setInt(4, studyPlan.getSub_file_id());
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
    public StudyPlan getStudyPlanByID(Integer rgno, String ID) throws SQLException {
        String sql = "SELECT * FROM STUDY_PLAN WHERE ID = ?";
        if (rgno != null)
            sql += "AND RGNO=?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StudyPlan studyPlan = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            preparedStatement.setString(1, ID);
            if (rgno != null)
                preparedStatement.setInt(2, rgno);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                studyPlan = convertToInstance(resultSet);
            }
            setUploadFiles(Collections.singletonList(studyPlan));
            return studyPlan;
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

    private static StudyPlan convertToInstance(ResultSet resultSet) throws SQLException {
        return new StudyPlan(
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
    public List<StudyPlan> getByStuInfo(Integer year, Integer sem, List<Integer> grade) {

        StringBuilder sql = new StringBuilder("SELECT A.*, R.GRADE, I.IDNO\n" +
                "FROM STUDY_PLAN A\n" +
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
                List<StudyPlan> ret = new ArrayList<>();
                try (ResultSet resultSet = ps.executeQuery()) {
                    while (resultSet.next()) {
                        StudyPlan plan = convertToInstance(resultSet);
                        ObjectUtils.setField(plan, "idno", resultSet.getString("IDNO"));
                        ret.add(plan);
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
    private void setUploadFiles(List<StudyPlan> list) {
        UploadFileDao uploadFileDao = new UploadFileDaoImpl(this);
        Map<Integer, UploadFile> map =  new HashMap<>();
        List<Integer> ids = new ArrayList<>();
        for (StudyPlan crd : list) {
            ids.add(crd.getMain_file_id());
            if(crd.getSub_file_id()!=null && crd.getSub_file_id()!=0)
            {
                ids.add(crd.getSub_file_id());
            }
        }
        for (UploadFile file : uploadFileDao.getByIds(ids)) {
            map.put(file.getId(),file);
        }
        for(StudyPlan crd : list)
        {
            crd.setMain_uploadFile(map.get(crd.getMain_file_id()));
            crd.setSub_uploadFile(map.get(crd.getSub_file_id()));
        }
    }
}

