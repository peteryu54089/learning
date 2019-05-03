package model;

import util.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Attend {

    private String rgno;
    private String sbj_year;
    private String sbj_sem;
    private Date date;
    private String ar_code;
    private String ar_Cname;
    private int period;
    private String sprd_name;

    public Attend() {
    }

    public Attend(String rgno, String sbj_year, String sbj_sem, Date date, String ar_code, String ar_Cname, int period, String sprd_name) {
        this.rgno = rgno;
        this.sbj_year = sbj_year;
        this.sbj_sem = sbj_sem;
        this.date = date;
        this.ar_code = ar_code;
        this.ar_Cname = ar_Cname;
        this.period = period;
        this.sprd_name = sprd_name;
    }


    public String getDateString() {
        return DateUtils.formatDate(date);
    }

    public String getDateADDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public String getRgno() {
        return rgno;
    }

    public void setRgno(String rgno) {
        this.rgno = rgno;
    }

    public String getSbj_year() {
        return sbj_year;
    }

    public void setSbj_year(String sbj_year) {
        this.sbj_year = sbj_year;
    }

    public String getSbj_sem() {
        return sbj_sem;
    }

    public void setSbj_sem(String sbj_sem) {
        this.sbj_sem = sbj_sem;
    }

    public String getAr_code() {
        return ar_code;
    }

    public void setAr_code(String ar_code) {
        this.ar_code = ar_code;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }


    public String getAr_Cname() {
        return ar_Cname;
    }

    public void setAr_Cname(String ar_Cname) {
        this.ar_Cname = ar_Cname;
    }

    public String getSprd_name() {
        return sprd_name;
    }

    public void setSprd_name(String sprd_name) {
        this.sprd_name = sprd_name;
    }
}
