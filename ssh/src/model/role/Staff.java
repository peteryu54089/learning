package model.role;

public abstract class Staff extends Role {

    private String staffCode;
    private String divCname;

    protected Staff(String register_number, String staffCode, String divCname) {
        super(register_number);
        this.staffCode = staffCode;
    }

    protected Staff(String register_number, String staffCode) {
        this(register_number, staffCode, "");
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }
}
