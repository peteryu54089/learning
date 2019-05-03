/**
 * 台北科技大學 計算機與網路中心 校務資訊組
 * @author WSY
 * 檔案名稱：EpApiConfig.java
 * 說明：學習歷程EP介接設定類別
 * 創建日期：2018年12月20日
 */
package config;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import ep.model.SchoolEPInfo;

/**
 * @author WSY
 *
 */
public class EpApiConfig {
	private Map<String, SchoolEPInfo> schoolEpConfigMap = new HashMap<String, SchoolEPInfo>();
	private String epAccountSuffix = "";
	private String epFTPIP = "";
	private String epAllowSbjYear = "";
	private String epAllowSbjSem = "";
	private String filesRootDir = "";
	private String oriFilesDir = "";
	private String epFilesDir = "";

	
	public EpApiConfig(String configFileName) {
		try {
			InputStream inStream = getClass().getClassLoader().getResourceAsStream(configFileName);
	        SAXReader reader = new SAXReader();
	        Document document = reader.read(inStream);
	        
	        Node epApiInfo = document.selectSingleNode("/CONFIG/EPAPIINFO");
	        this.epAccountSuffix = epApiInfo.valueOf("EPACCOUNTSUFFIX");
	        this.epFTPIP = epApiInfo.valueOf("FTPIP");
	        this.epAllowSbjYear = epApiInfo.valueOf("EPSBJYEAR");
	        this.epAllowSbjSem = epApiInfo.valueOf("EPSBJSEM");
	        
	        Node epFilesRootDirInfo = document.selectSingleNode("/CONFIG/EPFILESDIRINFO");
	        this.filesRootDir = epFilesRootDirInfo.valueOf("FILESROOTDIR");
	        this.oriFilesDir = epFilesRootDirInfo.valueOf("ORIFILESDIR");
	        this.epFilesDir = epFilesRootDirInfo.valueOf("EPFILESDIR");
	        
	        
	        List<Node> schoolNodes = document.selectNodes("/CONFIG/SCHOOL");
	        for (Node node : schoolNodes) {
	        	SchoolEPInfo schEPInfo = new SchoolEPInfo();
	        	schEPInfo.setSchoolID(node.valueOf("SID"));
	        	schEPInfo.setEpApiKey(node.valueOf("APIKEY"));
	        	schEPInfo.setEpApiAccount(node.valueOf("SID") + this.epAccountSuffix);;
	        	schEPInfo.setEpApiPassword(node.valueOf("APIPW"));
	        	schEPInfo.setEpFtpAccount(node.valueOf("FTPACCOUNT"));
	        	schEPInfo.setEpFtpPassword(node.valueOf("FTPPW"));

	        	this.schoolEpConfigMap.put(schEPInfo.getSchoolID(), schEPInfo);
	        }

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public Map<String, SchoolEPInfo> getSchoolEpConfigMap() {
		return schoolEpConfigMap;
	}
	public void setSchoolEpConfigMap(Map<String, SchoolEPInfo> schoolEpConfigMap) {
		this.schoolEpConfigMap = schoolEpConfigMap;
	}
	public String getEpAccountSuffix() {
		return epAccountSuffix;
	}
	public void setEpAccountSuffix(String epAccountSuffix) {
		this.epAccountSuffix = epAccountSuffix;
	}
	public String getEpFTPIP() {
		return epFTPIP;
	}
	public void setEpFTPIP(String epFTPIP) {
		this.epFTPIP = epFTPIP;
	}
	public String getEpAllowSbjYear() {
		return epAllowSbjYear;
	}
	public void setEpAllowSbjYear(String epAllowSbjYear) {
		this.epAllowSbjYear = epAllowSbjYear;
	}
	public String getEpAllowSbjSem() {
		return epAllowSbjSem;
	}
	public void setEpAllowSbjSem(String epAllowSbjSem) {
		this.epAllowSbjSem = epAllowSbjSem;
	}
	public String getFilesRootDir() {
		return filesRootDir;
	}
	public void setFilesRootDir(String filesRootDir) {
		this.filesRootDir = filesRootDir;
	}


	public String getOriFilesDir() {
		return oriFilesDir;
	}


	public void setOriFilesDir(String oriFilesDir) {
		this.oriFilesDir = oriFilesDir;
	}


	public String getEpFilesDir() {
		return epFilesDir;
	}


	public void setEpFilesDir(String epFilesDir) {
		this.epFilesDir = epFilesDir;
	}

}
