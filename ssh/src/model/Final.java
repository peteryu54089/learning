package model;

/**
 * Created by David on 2017/10/13.
 */
public class Final {

    private int id;
    private String id_no;
    private String type;
    private int tid;

    public Final(int id, String id_no, String type, int tid) {
        this.id = id;
        this.id_no = id_no;
        this.type = type;
        this.tid = tid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getId_no() {
        return id_no;
    }

    public void setId_no(String id_no) {
        this.id_no = id_no;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }
}
