package model;

import java.sql.Timestamp;
import java.util.List;

public class GroupCounsel extends Counsel {

    private List<GroupCounselMember> groupCounselMemberList;

    public GroupCounsel(int id, Timestamp startTime, Timestamp endTime, String title,String originalFilename,  Integer fileId, String location, String description, List<GroupCounselMember> groupCounselMemberList, String counselor, String submitter, Timestamp createdAt) {
        super(id, startTime, endTime, title, originalFilename, fileId, location, description, counselor, submitter, createdAt);
        this.groupCounselMemberList = groupCounselMemberList;
    }

    public List<GroupCounselMember> getGroupCounselMember() {
        return groupCounselMemberList;
    }

    public void setGroupCounselMember(List<GroupCounselMember> groupCounselMemberList) {
        this.groupCounselMemberList = groupCounselMemberList;
    }

    public int getTotal() {
        return this.groupCounselMemberList.size();
    }
}
