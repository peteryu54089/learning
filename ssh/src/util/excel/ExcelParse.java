/*
 * ExcelParse.java
 *
 * 2016-1-6 下午4:45:53
 *
 * RecluseKapoor
 *
 * Copyright © 2016, RecluseKapoor. All rights reserved.
 *
 */
package util.excel;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @Title:recluse-Excel文件解析工具类（兼容2003和2007版本Excel）
 * @Description: 该工具类用于解析Excel文件，同时兼容2003版和2007版Excel文件的解析，且随时可以进行新版本的扩展，
 * <p>
 * 若要支持新版本Excel格式的解析，只需要在excle包下新增一个实现了IExcelParse接口的实现类，
 * <p>
 * 在新增的实现类中实现新对版本Excel格式的解析的功能代码即可 ； 该扩展方法可以最大程度的实现解耦 。
 * <p>
 * @Company: 卡普工作室
 * @Website: http://www.cnblogs.com/reclusekapoor/
 * @author: RecluseKapoor
 * @CreateDate：2016-1-6 下午9:43:56
 * @version: 1.0
 * @lastModify:
 */
public class ExcelParse implements AutoCloseable {

    private IExcelParse excelParse = null;

    /**
     * 加载实例，根据路徑判斷不同版本的Excel文件，加载不同的具体实现实例
     *
     * @param path
     * @return
     */
    private boolean getInstance(String path) throws Exception {
        String ext = FilenameUtils.getExtension(path).toLowerCase();

        return getInstance(path, ext);
    }

    /**
     * 加载实例，根据不同版本的Excel文件，加载不同的具体实现实例
     *
     * @param path
     * @return
     */
    private boolean getInstance(String path, String extension) throws Exception {
        extension = extension.toLowerCase();
        if (extension.equals("xls")) {
            excelParse = new ExcelParse2003();
        } else if (extension.equals("xlsx")) {
            excelParse = new ExcelParse2007();
        } else {
            throw new Exception("对不起，目前系统不支持对该版本Excel文件的解析。");
        }
        return true;
    }

    /**
     * 获取excel工作区
     *
     * @param filePathAndName
     * @throws Exception
     */
    public void loadExcel(String filePathAndName) throws Exception {
        getInstance(filePathAndName);
        excelParse.loadExcel(filePathAndName);
    }

    /**
     * 获取excel工作区
     *
     * @param inputStream
     * @throws Exception
     */
    public void loadExcel(String path, InputStream inputStream) throws Exception {
        getInstance(path);
        excelParse.loadExcel(inputStream);
    }

    /**
     * 获取excel工作区
     *
     * @param filePathAndName
     * @param extension
     * @throws Exception
     */
    public void loadExcel(String filePathAndName, String extension) throws Exception {
        getInstance(filePathAndName, extension);
        excelParse.loadExcel(filePathAndName);
    }

    /**
     * 获取sheet页名称
     *
     * @param sheetNo
     * @return
     */
    public String getSheetName(int sheetNo) {
        return excelParse.getSheetName(sheetNo);
    }

    /**
     * 获取sheet页数
     *
     * @return
     * @throws Exception
     */
    public int getSheetCount() throws Exception {
        return excelParse.getSheetCount();
    }

    /**
     * 获取sheetNo页行数
     *
     * @param sheetNo
     * @return
     */
    public int getRowCount(int sheetNo) {
        return excelParse.getRowCount(sheetNo);
    }

    /**
     * 获取sheetNo页行数(含有操作或者内容的真实行数)
     *
     * @param sheetNo
     * @return
     */
    public int getRealRowCount(int sheetNo) {
        return excelParse.getRealRowCount(sheetNo);
    }

    /**
     * 读取第sheetNo个sheet页中第rowNo行第cellNo列的数据
     *
     * @param sheetNo sheet页编号
     * @param rowNo   行号
     * @param cellNo  列号
     * @return 返回相应的excel单元格内容
     * @throws Exception
     */
    public String readExcelByRowAndCell(int sheetNo, int rowNo, int cellNo)
            throws Exception {
        return excelParse.readExcelByRowAndCell(sheetNo, rowNo, cellNo);
    }

    /**
     * 读取指定SHEET页指定行的Excel内容
     *
     * @param sheetNo 指定SHEET页
     * @param rowNo   指定行
     * @return
     * @throws Exception
     */
    public String[] readExcelByRow(int sheetNo, int rowNo) throws Exception {
        return excelParse.readExcelByRow(sheetNo, rowNo);
    }

    /**
     * 读取指定SHEET页指定列中的数据
     *
     * @param sheetNo 指定SHEET页
     * @param cellNo  指定列号
     * @return
     * @throws Exception
     */
    public String[] readExcelByCell(int sheetNo, int cellNo) throws Exception {
        return excelParse.readExcelByCell(sheetNo, cellNo);
    }

    /**
     * 关闭excel工作区，释放资源
     */
    public void close() {
        excelParse.close();
    }

    /**
     * 將資料寫入指定的儲存格
     *
     * @param sheetNo sheet No
     * @param rowNo   rowNo
     * @param cellNo  cellNo
     * @param value   要寫入的資料
     * @throws Exception
     */
    public Cell writeExcelByRowAndCell(int sheetNo, int rowNo, int cellNo, boolean value) throws Exception {
        return excelParse.writeExcelByRowAndCell(sheetNo, rowNo, cellNo, value);
    }

    /**
     * 將資料寫入指定的儲存格
     *
     * @param sheetNo sheet No
     * @param rowNo   rowNo
     * @param cellNo  cellNo
     * @param value   要寫入的資料
     * @throws Exception
     */
    public Cell writeExcelByRowAndCell(int sheetNo, int rowNo, int cellNo, java.util.Calendar value) throws Exception {
        return excelParse.writeExcelByRowAndCell(sheetNo, rowNo, cellNo, value);
    }

    /**
     * 將資料寫入指定的儲存格
     *
     * @param sheetNo sheet No
     * @param rowNo   rowNo
     * @param cellNo  cellNo
     * @param value   要寫入的資料
     * @throws Exception
     */
    public Cell writeExcelByRowAndCell(int sheetNo, int rowNo, int cellNo, java.util.Date value) throws Exception {
        return excelParse.writeExcelByRowAndCell(sheetNo, rowNo, cellNo, value);
    }

    /**
     * 將資料寫入指定的儲存格
     *
     * @param sheetNo sheet No
     * @param rowNo   rowNo
     * @param cellNo  cellNo
     * @param value   要寫入的資料
     * @throws Exception
     */
    public Cell writeExcelByRowAndCell(int sheetNo, int rowNo, int cellNo, double value) throws Exception {
        return excelParse.writeExcelByRowAndCell(sheetNo, rowNo, cellNo, value);
    }

    /**
     * 將資料寫入指定的儲存格
     *
     * @param sheetNo sheet No
     * @param rowNo   rowNo
     * @param cellNo  cellNo
     * @param value   要寫入的資料
     * @throws Exception
     */
    public Cell writeExcelByRowAndCell(int sheetNo, int rowNo, int cellNo, org.apache.poi.ss.usermodel.RichTextString value) throws Exception {
        return excelParse.writeExcelByRowAndCell(sheetNo, rowNo, cellNo, value);
    }

    /**
     * 將資料寫入指定的儲存格
     *
     * @param sheetNo sheet No
     * @param rowNo   rowNo
     * @param cellNo  cellNo
     * @param value   要寫入的資料
     * @throws Exception
     */
    public Cell writeExcelByRowAndCell(int sheetNo, int rowNo, int cellNo, String value) throws Exception {
        return excelParse.writeExcelByRowAndCell(sheetNo, rowNo, cellNo, value);
    }

    /**
     * 取得指定的儲存格
     *
     * @param sheetNo sheet No
     * @param rowNo   rowNo
     * @param cellNo  cellNo
     * @throws Exception
     */
    public Cell getExcelCellByRowAndCell(int sheetNo, int rowNo, int cellNo) throws Exception {
        return excelParse.getExcelCellByRowAndCell(sheetNo, rowNo, cellNo, false);
    }

    public void write(OutputStream os) throws IOException {
        excelParse.write(os);
    }

    public Workbook getWorkBook() {
        return excelParse.getWorkBook();
    }
}