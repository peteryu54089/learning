package model;


import java.util.LinkedHashMap;
import java.util.Map;

public class Field {
    private Map<String, String> HashMap = new LinkedHashMap<>();

    public Field() {
        HashMap.put("11", "國語文領域");
        HashMap.put("12", "英語文領域");
        HashMap.put("21", "數學領域");
        HashMap.put("31", "歷史領域");
        HashMap.put("32", "地理領域");
        HashMap.put("33", "公民與社會領域");
        HashMap.put("41", "物理領域");
        HashMap.put("42", "化學領域");
        HashMap.put("43", "生物領域");
        HashMap.put("44", "地球科學領域");
        HashMap.put("51", "音樂領域");
        HashMap.put("52", "美術領域");
        HashMap.put("53", "藝術生活領域");
        HashMap.put("61", "生命教育領域");
        HashMap.put("62", "生涯規劃領域");
        HashMap.put("63", "家政領域");
        HashMap.put("71", "生活科技領域");
        HashMap.put("72", "資訊科技領域");
        HashMap.put("81", "健康與護理領域");
        HashMap.put("82", "體育領域");
        HashMap.put("91", "全民國防教育領域");
        HashMap.put("00", "跨領域或不分領域或不在上述之其他領域所列");

    }

    public Map<String, String> getHashMap() {
        return HashMap;
    }

    public void setHashMap(Map<String, String> hashMap) {
        HashMap = hashMap;
    }
}
