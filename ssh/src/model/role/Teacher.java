package model.role;

/**
 * Created by David on 2017/8/6.
 */
public class Teacher extends Staff {
    public Teacher(String register_number, String staffCode) {
        super(register_number, staffCode);
    }

    @Override
    public String getMenuBar() {
        return "menuBar/teacherBar.jsp";
    }
}
