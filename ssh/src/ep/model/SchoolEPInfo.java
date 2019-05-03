/**
 * 台北科技大學 計算機與網路中心 校務資訊組
 * @author WSY
 * 檔案名稱：SchoolEPInfo.java
 * 說明：學習歷程各校EP介接資訊類別
 * 創建日期：2018年12月21日
 */
package ep.model;

/**
 * @author WSY
 *
 */
public class SchoolEPInfo {
	private String schoolID = "";
	private String epApiKey = "";
	private String epApiAccount = "";
	private String epApiPassword = "";
	private String epFtpAccount = "";
	private String epFtpPassword = "";
	
	
	public String getSchoolID() {
		return schoolID;
	}
	public void setSchoolID(String schoolID) {
		this.schoolID = schoolID;
	}
	public String getEpApiKey() {
		return epApiKey;
	}
	public void setEpApiKey(String epApiKey) {
		this.epApiKey = epApiKey;
	}
	public String getEpApiAccount() {
		return epApiAccount;
	}
	public void setEpApiAccount(String epApiAccount) {
		this.epApiAccount = epApiAccount;
	}
	public String getEpApiPassword() {
		return epApiPassword;
	}
	public void setEpApiPassword(String epApiPassword) {
		this.epApiPassword = epApiPassword;
	}
	public String getEpFtpAccount() {
		return epFtpAccount;
	}
	public void setEpFtpAccount(String epFtpAccount) {
		this.epFtpAccount = epFtpAccount;
	}
	public String getEpFtpPassword() {
		return epFtpPassword;
	}
	public void setEpFtpPassword(String epFtpPassword) {
		this.epFtpPassword = epFtpPassword;
	}

}
