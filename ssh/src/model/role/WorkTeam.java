package model.role;

public class WorkTeam extends Staff {
    public WorkTeam(String register_number, String staffCode) {
        super(register_number, staffCode, "");
    }

    @Override
    public String getMenuBar() {
        return "menuBar/workTeamBar.jsp";
    }
}
