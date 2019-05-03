package model.role;

/**
 * Created by Ching Yun Yu on 2018/4/27.
 */
public class CourseSubmitter extends Role {
    private String staff_code;

    public CourseSubmitter(String register_number, String staff_code) {
        super(register_number);
        this.staff_code = staff_code;
    }

    public String getStaffCode() {
        return staff_code;
    }

    public void setStaffCode(String staff_code) {
        this.staff_code = staff_code;
    }

    @Override
    public String getMenuBar() {
        return "menuBar/courseSubmitterBar.jsp";
    }
}
