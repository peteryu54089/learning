package model.role;

/**
 * Created by Ching Yun Yu on 2018/4/27.
 */
public class Counselor extends Staff {
    public Counselor(String register_number, String staff_code) {
        super(register_number, staff_code);
    }

    @Override
    public String getMenuBar() {
        return "menuBar/counselorBar.jsp";
    }

}
