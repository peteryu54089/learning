package dao;

import model.GroupCounsel;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public interface GroupCounselDao {
    List<GroupCounsel> getGroupCounselByTitleOrDate(Date startTime, Date endTime, String title) throws SQLException;

    List<GroupCounsel> getGroupCounselByTitleOrDateAndRegNo(Date startTime, Date endTime, String title, String regNo) throws SQLException;

    void insertGroupCounsel(GroupCounsel groupCounsel) throws SQLException;

    void deleteGroupCounsel(int id) throws SQLException;

    GroupCounsel getGroupCounselById(int id) throws SQLException;

    void updateGroupCounsel(GroupCounsel groupCounsel) throws SQLException;

}
