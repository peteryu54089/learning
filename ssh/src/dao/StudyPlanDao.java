package dao;


import model.StudyPlan;

import java.sql.SQLException;
import java.util.List;

public interface StudyPlanDao {
    void uploadStudyPlan(Integer rgno, String topic, String description, Integer main_file_id, Integer sub_file_id, Integer term_year, Integer term_semester) throws SQLException;

    void deleteStudyPlan(Integer rgno, String ID) throws SQLException;

    Integer getPageNumber(Integer rgno) throws SQLException;

    List<StudyPlan> getStudyPlan(Integer rgno) throws SQLException;

    List<StudyPlan> getStudyPlan(Integer rgno, Integer page) throws SQLException;

    void updateStudyPlanByID(Integer rgno, String ID, StudyPlan studyPlan) throws SQLException;

    StudyPlan getStudyPlanByID(Integer rgno, String ID) throws SQLException;

    List<StudyPlan> getByStuInfo(Integer year, Integer sem, List<Integer> grade);
}