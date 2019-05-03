/**
 * 台北科技大學 計算機與網路中心 校務資訊組
 * @author WSY
 * 檔案名稱：Call4UploadfileReadyApi.java
 * 說明：通知暨大檔案取得位置
 * 創建日期：2018年12月20日
 */
package app;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;

import config.EpApiConfig;
import ep.EPApi;
import ep.model.ResponseFID;
import ep.model.SchoolEPInfo;
import util.LoggerUtils;


/**
 * @author WSY
 *
 */
public class Call4UploadfileReadyApi {
	
	public static void main(String[] args) throws Exception {
		Call4UploadfileReadyApi c4urApp = new Call4UploadfileReadyApi();
		c4urApp.run(new String[]{"EpApiConfig.xml"});
//		c4urApp.run(args);
	}

	public void run(String[] pArgs) throws IOException {
		EpApiConfig epApiConfig = new EpApiConfig(pArgs[0]);
		Map<String, SchoolEPInfo> schoolEpConfigMap = epApiConfig.getSchoolEpConfigMap();
		String ftpIP = epApiConfig.getEpFTPIP();
		String epAllowSbjYear = epApiConfig.getEpAllowSbjYear();
		String epAllowSbjSem = epApiConfig.getEpAllowSbjSem();
		String filesRootPath = epApiConfig.getFilesRootDir();
		String oriFileRootPath = filesRootPath + File.separator + epApiConfig.getOriFilesDir();
		String epFilesRootPath = filesRootPath + File.separator + epApiConfig.getEpFilesDir();
//		System.out.println("oriFileRootDir: " + oriFileRootPath);
//		System.out.println("epFilesRootPath: " + epFilesRootPath);
		
		for(String sid : schoolEpConfigMap.keySet()){
			System.out.println("sid: " + sid);
			
			String schOriFileDirPath = oriFileRootPath + File.separator + sid;
			String schEPFileDirPath = epFilesRootPath + File.separator + sid;
			File schOriFileDir = new File(schOriFileDirPath);
			if(schOriFileDir.exists() && schOriFileDir.isDirectory()){
				String[] fileList = schOriFileDir.list();
				if(fileList!=null && fileList.length>0){
					SchoolEPInfo schInfo = schoolEpConfigMap.get(sid);
//					System.out.println("apiKey: " + schInfo.getEpApiKey());
//					System.out.println("epAccount: " + schInfo.getEpApiAccount());
//					System.out.println("ftpIP: " + ftpIP);
//					System.out.println("ftpAccount: " + schInfo.getEpFtpAccount());
//					System.out.println("ftpPassword: " + schInfo.getEpFtpPassword());
					
					for(int i=0; i<fileList.length; i++){
//						System.out.println("file: " + fileList[i]);
						
						File oriFile = new File(schOriFileDirPath + File.separator + fileList[i]);
						if(oriFile.exists() && oriFile.isFile()){
							String rollNum = FilenameUtils.getBaseName(oriFile.getName());
//							System.out.println("rollNum: " + rollNum);
							
//							if(this.chcekIsInWait4ULList(sid, rollNum)){
								EPApi api = new EPApi(schInfo.getEpApiKey(), schInfo.getEpApiAccount(), ftpIP, schInfo.getEpFtpAccount(), schInfo.getEpApiPassword(), schEPFileDirPath);
								ResponseFID rpFID = api.call4UploadfileReady(schInfo.getSchoolID(), epAllowSbjYear, epAllowSbjSem, rollNum, oriFile);
//							}
						}
					}
				}
			}
		}
	}
	
	public boolean chcekIsInWait4ULList(String pSchCode, String pRollNum) {
		boolean result = false;
		
		return result;
	}
	
}
