package model;

import java.sql.ResultSet;

/**
 * 系統資料表
 * @author yujim
 * @since 2014/1/15
 */
public class MisSystemData {
	private int sys_num = 0;           // 系統編號
	private String sys_cname = "";     // 系統名稱
	private String sys_link = "";      // 系統連結
	private int sys_order = 0;         // 系統顯示順序
	private int sbj_year = 0;          // 操作學年
	private int sbj_sem = 0;       	   // 操作學期
	
	/**
	 * 取得Bean資料
	 * @param Prs ResultSet
	 * @return 筆數
	 * @throws Exception
	 * @author yujim
	 */
	public int getDataFromResultSet(ResultSet Prs) throws Exception {
        if (Prs != null) {
            this.setSys_num(Prs.getInt("sys_num"));
            this.setSys_cname(Prs.getString("sys_cname").trim());
            this.setSys_link(Prs.getString("sys_link").trim());
            this.setSys_order(Prs.getInt("sys_order"));
            this.setSbj_year(Prs.getInt("sbj_year"));
            this.setSbj_sem(Prs.getInt("sbj_sem"));
            return 1;
        } 
        return 0;
    }
	
	public int getSys_num() {
		return sys_num;
	}
	public void setSys_num(int sys_num) {
		this.sys_num = sys_num;
	}
	public String getSys_cname() {
		return sys_cname;
	}
	public void setSys_cname(String sys_cname) {
		this.sys_cname = sys_cname;
	}
	public String getSys_link() {
		return sys_link;
	}
	public void setSys_link(String sys_link) {
		this.sys_link = sys_link;
	}
	public int getSys_order() {
		return sys_order;
	}
	public void setSys_order(int sys_order) {
		this.sys_order = sys_order;
	}
	public int getSbj_year() {
		return sbj_year;
	}
	public void setSbj_year(int sbj_year) {
		this.sbj_year = sbj_year;
	}
	public int getSbj_sem() {
		return sbj_sem;
	}
	public void setSbj_sem(int sbj_sem) {
		this.sbj_sem = sbj_sem;
	}
}
