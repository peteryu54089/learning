package model.role;

public class Tutor extends Staff {
    public Tutor(String register_number, String staff_code) {
        super(register_number, staff_code);
    }

    @Override
    public String getMenuBar() {
        return "menuBar/tutorBar.jsp";
    }
}
