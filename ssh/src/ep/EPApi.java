package ep;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

import ep.model.*;
import util.LoggerUtils;


public class EPApi {
    private static final String BASE_URL = "https://ep.cloud.ncnu.edu.tw/iscom_api";
    private static final MediaType JSON = MediaType.parse("application/json");
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
    private static final SimpleDateFormat REQUEST_DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    private final String apiKey;
    private final String epAccount;
    private final String ip;
    private final String ftpAccount;
    private final String ftpPassword;
    private final String schEPFileDirPath;

    public EPApi(String apiKey, String epAccount, String ip, String ftpAccount, String ftpPassword, String schEPFileDirPath) {
        this.apiKey = apiKey;
        this.epAccount = epAccount;
        this.ip = ip;
        this.ftpAccount = ftpAccount;
        this.ftpPassword = ftpPassword;
        this.schEPFileDirPath = schEPFileDirPath;
    }
    
    public EPApi(String apiKey, String epAccount, String ip, String ftpAccount, String ftpPassword) {
    	this(apiKey, epAccount, ip, ftpAccount, ftpPassword, "ep_files");
    }

    public EPApi(String apiKey, String epAccount) {
        this(apiKey, epAccount, "163.22.21.75", "Administrator", "nRUUjMcjnR@5B0R!98BX", "ep_files");
    }
    
    private CryptographyLib getCryptographyLib() throws IOException, NullPointerException {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, "{}");
        Request request = new Request.Builder()
                .url(BASE_URL + "/iscom_get_public_key_api.ashx")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        String r = response.body().string();

        PublicKeyModel publicKeyModel = getGson().fromJson(r, PublicKeyModel.class);
//        System.out.println("GUID: " + publicKeyModel.GUID);
//        System.out.println("PublicKey: " + publicKeyModel.PublicKey);
        
        return new CryptographyLib(publicKeyModel.GUID, publicKeyModel.PublicKey);
    }

    private Response sendRequest(CryptographyLib lib, String url, Object obj) throws IOException {
        String json = getGson().toJson(obj);
        System.out.println("json: " + json);
        
        OkHttpClient client = new OkHttpClient();
        JsonDataFormat jsonDataFormat = new JsonDataFormat();
        jsonDataFormat.Status = true;
        jsonDataFormat.RequestNo = lib.getUuid();
        jsonDataFormat.RequestTime = "/Date(" + new Date().getTime() + ")/";
        jsonDataFormat.EncryptKey = lib.getEncryptedAesKeyBase64();
        jsonDataFormat.EncryptData = CryptographyLib.convertByteToDotNetByte(lib.encrypt(json));
        
        String reqEncJson = getGson().toJson(jsonDataFormat);
        System.out.println("reqEncJson: " + getGson().toJson(jsonDataFormat));

        RequestBody body = RequestBody.create(JSON, reqEncJson);
        Request request = new Request.Builder()
                .url(BASE_URL + url)
                .post(body)
                .build();
        return client.newCall(request).execute();
    }

    public ResponseFID sendFileRequest(String schoolId, String year, String sem, String listNo, File sourceFile) throws IOException {
        CryptographyLib lib = getCryptographyLib();
        final String ext = "." + FilenameUtils.getExtension(sourceFile.getAbsolutePath());
        FileInfo fileInfo = new FileInfo();
        fileInfo.APIKey = apiKey;
        fileInfo.學校IP = ip;
        fileInfo.FTP帳號 = ftpAccount;
        fileInfo.FTP密碼 = ftpPassword;
        fileInfo.EP帳號 = epAccount;
        fileInfo.上傳單位 = schoolId;
        fileInfo.學年度 = year;
        fileInfo.學期 = sem;
        fileInfo.名冊別 = listNo;
        fileInfo.副檔名 = ext;
        fileInfo.上傳時間 = SIMPLE_DATE_FORMAT.format(new Date());
        fileInfo.狀態 = "add";

        String fileName = String.format("%s_%s_%s_%s_%s", fileInfo.上傳單位, fileInfo.學年度, fileInfo.學期, fileInfo.名冊別, fileInfo.上傳時間);
        
        Path tempDirWithPrefix = Files.createTempDirectory("EP");
        File tmpSrcFile = new File(tempDirWithPrefix.toFile().getAbsolutePath(), fileName + ext);
        try {
            Files.copy(sourceFile.toPath(), tmpSrcFile.toPath());

            CryptographyLib.EncryptedFile encryptedFile = lib.encryptedFile(tmpSrcFile);
            encryptedFile.encrypt();
            fileInfo.Signature = encryptedFile.getSign();
//            upLoadFileToFtp(encryptedFile.getTargetFile(), new File("/"));
//            upLoadFileToFtp(encryptedFile.getTargetFile(), new File("/" + fileInfo.FTP帳號));

            Response response = sendRequest(lib, "/iscom_call_for_uploadfile_ready_api.ashx", fileInfo);
            String responseStr = response.body().string();
            
            System.out.println("Response: " + responseStr);
            return getGson().fromJson(responseStr, ResponseFID.class);
        } finally {
//            FileUtils.deleteDirectory(tempDirWithPrefix.toFile());
        	System.out.println("-------------------------------------------------------------------------");
        }
    }

    public ResponseFID call4UploadfileReady(String schoolId, String year, String sem, String listNo, File sourceFile) throws IOException {
    	String responseStr = "";
    	
        CryptographyLib lib = getCryptographyLib();
        final String ext = "." + FilenameUtils.getExtension(sourceFile.getAbsolutePath());
        FileInfo fileInfo = new FileInfo();
        fileInfo.APIKey = apiKey;
        fileInfo.學校IP = ip;
        fileInfo.FTP帳號 = ftpAccount;
        fileInfo.FTP密碼 = ftpPassword;
        fileInfo.EP帳號 = epAccount;
        fileInfo.上傳單位 = schoolId;
        fileInfo.學年度 = year;
        fileInfo.學期 = sem;
        fileInfo.名冊別 = listNo;
        fileInfo.副檔名 = ext;
        fileInfo.上傳時間 = SIMPLE_DATE_FORMAT.format(new Date());
        fileInfo.狀態 = "add";

        String fileName = String.format("%s_%s_%s_%s_%s", fileInfo.上傳單位, fileInfo.學年度, fileInfo.學期, fileInfo.名冊別, fileInfo.上傳時間);

		File school_dir = new File(this.schEPFileDirPath);
		File school_ul_dir = new File(this.schEPFileDirPath + File.separator + fileInfo.上傳時間);
		if(!school_dir.exists()){
			school_dir.mkdirs();
		}
		if(!school_ul_dir.exists()){
			school_ul_dir.mkdirs();
		}	
		
		File tmpSrcFile = new File(school_ul_dir.getAbsolutePath(), fileName + ext);
        try {
            Files.copy(sourceFile.toPath(), tmpSrcFile.toPath());

            CryptographyLib.EncryptedFile encryptedFile = lib.encryptedFile(tmpSrcFile);
            encryptedFile.encrypt();
            fileInfo.Signature = encryptedFile.getSign();
            
            if(schoolId.equals("014300")){	// 測試帳號
//            	upLoadFileToFtp(encryptedFile.getTargetFile(), new File("/" + fileInfo.FTP帳號));
//				Response response = sendRequest(lib, "/iscom_call_for_uploadfile_ready_api.ashx", fileInfo);
//				responseStr = response.body().string();
//				System.out.println("Response: " + responseStr);
            } 
            System.out.println("sch: " + schoolId + ", 學年期: " + (year + "-" + sem) + ", 名冊別: " + listNo + " Finish");
            return getGson().fromJson(responseStr, ResponseFID.class);
        } catch (Exception ex) {
        	String msg = "sch: " + schoolId + ", 學年期: " + (year + "-" + sem) + ", 名冊別: " + listNo + ", Call4UploadfileReady Error: ";
        	System.out.println(LoggerUtils.format(msg, ex));
        	
        	return getGson().fromJson(responseStr, ResponseFID.class);
        } /*finally {
            FileUtils.deleteDirectory(school_ul_dir);
        	
        }*/
    }
    
    public ResponseData checkStatus(Integer fid) throws IOException {
        CryptographyLib lib = getCryptographyLib();
        FileStatus fileStatus = new FileStatus();
        fileStatus.APIKey = apiKey;
        fileStatus.FID = fid;
        fileStatus.Mode = "GET";

        Response response = sendRequest(lib, "/iscom_call_for_status_api.ashx", fileStatus);
        String responseStr = response.body().string();
        System.out.println("Response: " + responseStr);
        return getGson().fromJson(responseStr, ResponseData.class);
    }

    public boolean upLoadFileToFtp(File srcFile, File targetDir) {
        FTPClient client = new FTPClient();
        try {
            client.connect(ip);
            client.login(ftpAccount, ftpPassword);
            client.setFileType(FTPClient.BINARY_FILE_TYPE);
            client.enterLocalPassiveMode();
            client.makeDirectory(targetDir.getPath());

            boolean ret = false;
            client.enterLocalPassiveMode();
            String targetFullPath = new File(targetDir, srcFile.getName()).getPath();
            try (InputStream is = new FileInputStream(srcFile)) {
                ret = client.storeFile(targetFullPath, is);
            }

            if (!ret) {
                throw new IOException("Failed to upload file.\n" + client.getReplyString());
            }

            return ret;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    private static Gson getGson() {
        return new GsonBuilder().disableHtmlEscaping().create();
    }


}
