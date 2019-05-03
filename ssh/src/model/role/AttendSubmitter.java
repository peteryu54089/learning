package model.role;

public class AttendSubmitter extends Staff {
    public AttendSubmitter(String register_number, String staffCode) {
        super(register_number, staffCode, "");
    }

    @Override
    public String getMenuBar() {
        return "menuBar/attendSubmitterBar.jsp";
    }
}
