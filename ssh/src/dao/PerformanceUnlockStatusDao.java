package dao;

public interface PerformanceUnlockStatusDao {
    boolean isInUnlockStatus(String activeYear, Integer rgno);

    void grantUnlock(String activeYear, Integer rgno);

    void revokeUnlock(String activeYear, Integer rgno);
}
