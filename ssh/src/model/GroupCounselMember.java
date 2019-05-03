package model;

public class GroupCounselMember {
    private int groupCounselId;
    private int registerNumber;

    public GroupCounselMember(int groupCounselId, int registerNumber) {
        this.groupCounselId = groupCounselId;
        this.registerNumber = registerNumber;

    }

    public int getGroupCounselId() {
        return groupCounselId;
    }

    public void setGroupCounselId(int groupCounselId) {
        this.groupCounselId = groupCounselId;
    }

    public int getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(int registerNumber) {
        this.registerNumber = registerNumber;
    }
}
