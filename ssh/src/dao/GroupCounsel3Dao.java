package dao;

import model.GroupCounsel3;

import java.sql.SQLException;
import java.util.List;

public interface GroupCounsel3Dao {
    List<GroupCounsel3> getGroupCounsel3ByText1AndText2(String text1, String text2) throws SQLException;

    void deleteGroupCounsel3(String text1, String text2, String label1, String label2, String label3, String label4, String label5, String label6) throws SQLException;
}
