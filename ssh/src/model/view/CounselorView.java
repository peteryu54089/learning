package model.view;

public class CounselorView {
    private String staffCode;
    private String name;

    public CounselorView(String staffCode, String name) {
        this.staffCode = staffCode;
        this.name = name;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public String getName() {
        return name;
    }
}
