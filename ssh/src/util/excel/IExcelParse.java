package util.excel;
/*
 * IExcelParse.java
 *
 * 2016-1-6 下午4:45:53
 *
 * RecluseKapoor
 *
 * Copyright © 2016, RecluseKapoor. All rights reserved.
 *
 */

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.InputStream;

/**
 * @Title: recluse-Excel文件解析接口
 * @Description:Excel文件解析接口，所有版本的Excel解析类都要实现该接口
 * @Company: 卡普工作室
 * @Website: http://www.cnblogs.com/reclusekapoor/
 * @author: RecluseKapoor
 * @CreateDate：2016-1-6 下午9:42:08
 * @version: 1.0
 * @lastModify:
 */
public interface IExcelParse extends AutoCloseable {
    public void loadExcel(String path) throws Exception;

    public void loadExcel(InputStream inputStream) throws Exception;

    public String getSheetName(int sheetNo);

    public int getSheetCount() throws Exception;

    public int getRowCount(int sheetNo);

    public int getRealRowCount(int sheetNo);

    public String readExcelByRowAndCell(int sheetNo, int rowNo, int cellNo)
            throws Exception;

    public String[] readExcelByRow(int sheetNo, int rowNo) throws Exception;

    public String[] readExcelByCell(int sheetNo, int cellNo) throws Exception;

    public void close();

    public Cell writeExcelByRowAndCell(int sheetNo, int rowNo, int cellNo, boolean value) throws Exception;

    public Cell writeExcelByRowAndCell(int sheetNo, int rowNo, int cellNo, java.util.Calendar value) throws Exception;

    public Cell writeExcelByRowAndCell(int sheetNo, int rowNo, int cellNo, java.util.Date value) throws Exception;

    public Cell writeExcelByRowAndCell(int sheetNo, int rowNo, int cellNo, double value) throws Exception;

    public Cell writeExcelByRowAndCell(int sheetNo, int rowNo, int cellNo, org.apache.poi.ss.usermodel.RichTextString value) throws Exception;

    public Cell writeExcelByRowAndCell(int sheetNo, int rowNo, int cellNo, String value) throws Exception;

    public void write(java.io.OutputStream os) throws java.io.IOException;

    public Cell getExcelCellByRowAndCell(int sheetNo, int rowNo, int cellNo, boolean createIfNotExists) throws Exception;

    public Workbook getWorkBook();
}
