/**
 * 台北科技大學 計算機與網路中心 校務資訊組
 * @author WSY
 * 檔案名稱：FilePartUtils.java
 * 說明：For Servlet 3.0  
 * 創建日期：2019年1月3日
 */
package util;

import javax.servlet.http.Part;

/**
 * @author WSY
 *
 */
public class FilePartUtils {
	
	/**
     * 從 HTTP header content-disposition 中取得文件名
     * content-disposition：form-data; name="dataFile"; filename="PHOTO.JPG"
     */
	public static String getSubmittedFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length()-1);
            }
        }
        return "";
    }
}
