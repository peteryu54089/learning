package dao;

public interface CourseRecordUnlockStatusDao {
    boolean isInUnlockStatus(String activeYear, String sem, Integer rgno);

    void grantUnlock(String activeYear, String sem, Integer rgno);

    void revokeUnlock(String activeYear, String sem, Integer rgno);
}
