package model;

public class ArCode {

    private String ar_code;
    private String ar_cname;
    private String ar_use;
    private String ar_type;


    public ArCode() {
    }

    public ArCode(String ar_code, String ar_cname, String ar_use, String ar_type) {
        this.ar_code = ar_code;
        this.ar_cname = ar_cname;
        this.ar_use = ar_use;
        this.ar_type = ar_type;
    }

    public String getAr_code() {
        return ar_code;
    }

    public void setAr_code(String ar_code) {
        this.ar_code = ar_code;
    }

    public String getAr_cname() {
        return ar_cname;
    }

    public void setAr_cname(String ar_cname) {
        this.ar_cname = ar_cname;
    }

    public String getAr_use() {
        return ar_use;
    }

    public void setAr_use(String ar_use) {
        this.ar_use = ar_use;
    }

    public String getAr_type() {
        return ar_type;
    }

    public void setAr_type(String ar_type) {
        this.ar_type = ar_type;
    }
}
