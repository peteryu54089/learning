/**
 * 台北科技大學 計算機與網路中心 校務資訊組
 * @author WSY
 * 檔案名稱：XXX.java
 * 說明：XXXX
 * 創建日期：2019年1月9日
 */
package util;

/**
 * @author WSY
 *
 */
public class SchemaUtil {
	public final static String hsaPreSchema = "HS";
	public final static String lrnPreSchema = "LRN";
//
//
//	public static String getSchCodeFromLRNSchema(String schoolId){
//    	String result = "";
//
//    	int beginIndex = pLRNSchema.length()-6;
//    	int endIndex = pLRNSchema.length();
//    	result = pLRNSchema.substring(beginIndex, endIndex);
//
//    	return result;
//    }
    
    public static String convertSchoolIdToHSASchema(String schoolId){
    	return  hsaPreSchema + schoolId;
    }
}
