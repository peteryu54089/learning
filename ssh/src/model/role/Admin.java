package model.role;

public class Admin extends Role {
    public Admin(String register_number) {
        super(register_number);
    }

    @Override
    public String getMenuBar() {
        return "menuBar/adminBar.jsp";
    }
}
