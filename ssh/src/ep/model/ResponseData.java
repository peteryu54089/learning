package ep.model;

import java.util.List;

public class ResponseData {
    public int FID;
    public int IsCheck;
    public int IsExtract;
    public boolean HaveErrorMsg;
    public String 處理狀態;
    public List<ErrorMsg> 錯誤訊息;
}
