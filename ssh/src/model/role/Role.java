package model.role;

public abstract class Role {
    protected final String regNumber;

    @Deprecated
    public String getRegNumber() {
        return regNumber;
    }

    Role(String register_number) {
        this.regNumber = register_number;
    }

    public abstract String getMenuBar();
    // abstract Role getDetailByRegNumber(String regNumber);
}
