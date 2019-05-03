package dao;

import model.Autobiography;

import java.sql.SQLException;
import java.util.List;

public interface AutobiographyDao {
    void uploadAutobiography(Integer rgno, String topic, String description, Integer main_file_id, Integer sub_file_id, Integer term_year, Integer term_semester) throws SQLException;

    void deleteAutobiography(Integer rgno, String ID) throws SQLException;

    Integer getPageNumber(Integer rgno) throws SQLException;

    List<Autobiography> getAutobiography(Integer rgno) throws SQLException;

    List<Autobiography> getAutobiography(Integer rgno, Integer page) throws SQLException;

    void updateAutobiographyByID(Integer rgno, String ID, Autobiography autobiography) throws SQLException;

    Autobiography getAutobiographyByID(Integer rgno, String ID) throws SQLException;

    List<Autobiography> getByStuInfo(Integer year, Integer sem, List<Integer> grade);
}
