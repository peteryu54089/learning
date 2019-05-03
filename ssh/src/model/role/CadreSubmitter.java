package model.role;

public class CadreSubmitter extends Staff {
    public CadreSubmitter(String register_number, String staffCode) {
        super(register_number, staffCode, "");
    }

    @Override
    public String getMenuBar() {
        return "menuBar/cadreSubmitterBar.jsp";
    }
}
