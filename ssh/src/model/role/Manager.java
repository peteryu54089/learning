package model.role;

public class Manager extends Staff {

    private String ip;

    public Manager(String register_number) {
        this(register_number, register_number,
                null, null);

    }

    public Manager(String register_number, String staffCode,
                   String ip, String div_cname) {
        super(register_number, staffCode);
        this.ip = ip;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        System.out.println("Setting login ip as " + ip);
        this.ip = ip;
    }

    @Override
    public String getMenuBar() {
        return "menuBar/managerBar.jsp";
    }
}
