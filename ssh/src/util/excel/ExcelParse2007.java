/*
 * ExcelParse2007.java
 *
 * 2016-1-6 下午4:45:53
 *
 * RecluseKapoor
 *
 * Copyright © 2016, RecluseKapoor. All rights reserved.
 *
 */
package util.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import util.LogUtility;

import java.io.*;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * @Title: recluse--2007版Excel文件解析工具
 * @Description: 解析2007版Excel文件具体实现类
 * @Company: 卡普工作室
 * @Website: http://www.cnblogs.com/reclusekapoor/
 * @author: RecluseKapoor
 * @CreateDate：2016-1-6 下午9:51:15
 * @version: 1.0
 * @lastModify:
 */
public class ExcelParse2007 implements IExcelParse {
    // Excel工作区
    private XSSFWorkbook _wb = null;

    /**
     * 加载excel文件，获取excel工作区
     *
     * @param filePathAndName
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void loadExcel(String filePathAndName) throws FileNotFoundException,
            IOException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePathAndName);
            _wb = new XSSFWorkbook(fis);
        } catch (FileNotFoundException e) {
            LogUtility.errorLog("Error when loading excel", e);
            throw new FileNotFoundException("加载Excel文件失败：" + e.getMessage());
        } catch (IOException e) {
            LogUtility.errorLog("Error when loading excel", e);
            throw new IOException("加载Excel文件失败：" + e.getMessage());
        } finally {
            if (fis != null) {
                fis.close();
                fis = null;
            }
        }
    }

    /**
     * 加载excel文件，获取excel工作区
     *
     * @param inputStream
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void loadExcel(InputStream inputStream) throws IOException {
        try {
            _wb = new XSSFWorkbook(inputStream);
        } catch (IOException e) {
            LogUtility.errorLog("Error when loading excel", e);
            throw new IOException("加载Excel文件失败：" + e.getMessage());
        } finally {
        }
    }

    /**
     * 获取sheet页名称
     *
     * @param sheetNo
     * @return
     */
    public String getSheetName(int sheetNo) {
        return getWorkBook().getSheetName(sheetNo - 1);
    }

    /**
     * 获取sheet页数
     *
     * @return int
     */
    public int getSheetCount() throws Exception {
        int sheetCount = getWorkBook().getNumberOfSheets();
        if (sheetCount == 0) {
            throw new Exception("Excel中没有SHEET页");
        }
        return sheetCount;
    }

    /**
     * 获取sheetNo页行数
     *
     * @param sheetNo
     * @return
     */
    public int getRowCount(int sheetNo) {
        int rowCount = 0;
        XSSFSheet sheet = (XSSFSheet) (getWorkBook().getSheetAt(sheetNo - 1));
        rowCount = sheet.getPhysicalNumberOfRows();
        return rowCount;
    }

    /**
     * 获取sheetNo页行数(含有操作或者内容的真实行数)
     *
     * @param sheetNo
     * @return
     */
    public int getRealRowCount(int sheetNo) {
        int rowCount = 0;
        int rowNum = 0;
        XSSFSheet sheet = (XSSFSheet) (getWorkBook().getSheetAt(sheetNo - 1));
        rowCount = sheet.getPhysicalNumberOfRows();
        if (rowCount == 0) {
            return rowCount;
        }
        XSSFRow row = null;
        XSSFCell cell = null;
        rowNum = rowCount;
        for (int i = 0; i < rowCount; i++) {
            row = sheet.getRow(rowNum - 1);
            rowNum--;
            if (row == null) {
                continue;
            }
            short firstCellNum = row.getFirstCellNum();
            short lastCellNum = row.getLastCellNum();
            for (int j = firstCellNum; j < lastCellNum; j++) {
                cell = row.getCell(j);
                if (cell == null) {
                    continue;
                } else if (cell.getCellType() == XSSFCell.CELL_TYPE_BLANK) {
                    continue;
                } else if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
                    String value = cell.getStringCellValue();
                    if (value == null || value.equals("")) {
                        continue;
                    } else {
                        value = value.trim();
                        if (value.isEmpty() || value.equals("")
                                || value.length() == 0) {
                            continue;
                        }
                    }
                }
                rowCount = rowNum + 1;
                return rowCount;
            }
        }

        rowCount = rowNum;
        return rowCount;
    }

    /**
     * 读取第sheetNo个sheet页中第rowNo行第cellNo列的数据(通过)
     *
     * @param sheetNo sheet页编号
     * @param rowNo   行号
     * @param cellNo  列号
     * @return 返回相应的excel单元格内容
     * @throws Exception
     */
    public String readExcelByRowAndCell(int sheetNo, int rowNo, int cellNo)
            throws Exception {
        String rowCellData = "";
        String sheetName = getWorkBook().getSheetName(sheetNo - 1);
        try {
            XSSFCell cell = (XSSFCell) getExcelCellByRowAndCell(sheetNo, rowNo, cellNo, false);

            int cellType = cell.getCellType();
            String df = cell.getCellStyle().getDataFormatString();
            if (cellType == XSSFCell.CELL_TYPE_NUMERIC) {// 数值(包括excel中数值、货币、日期、时间、会计专用等单元格格式)
                double d = cell.getNumericCellValue();
                // 判断数值是否是日期，该方法只能识别部分日期格式,故加入第二个判断条件对不能识别的日期再次进行识别
                if (DateUtil.isCellDateFormatted(cell)
                        || df.contains("yyyy\"年\"m\"月\"d\"日\"")) {// 日期、时间单元格格式
                    Date date = DateUtil.getJavaDate(d);
                    Timestamp timestamp = new Timestamp(date.getTime());
                    String temp = timestamp.toString();
                    if (temp.endsWith("00:00:00.0")) {// yyyy-MM-dd 格式
                        rowCellData = temp.substring(0,
                                temp.lastIndexOf("00:00:00.0"));
                    } else if (temp.endsWith(".0")) {// yyyy-MM-dd hh:mm:ss 格式
                        rowCellData = temp.substring(0, temp.lastIndexOf(".0"));
                    } else {
                        rowCellData = timestamp.toString();
                    }
                } else {// 数值、货币、会计专用、百分比、分数、科学记数 单元格式
                    rowCellData = new DecimalFormat("0.########").format(d);
                }
            } else if (cellType == XSSFCell.CELL_TYPE_STRING) {// 文本
                rowCellData = cell.getStringCellValue();
            } else if (cellType == XSSFCell.CELL_TYPE_FORMULA) {// 公式
                double d = cell.getNumericCellValue();
                rowCellData = String.valueOf(d);
            } else if (cellType == XSSFCell.CELL_TYPE_BLANK) {// 空
                rowCellData = "";
            } else if (cellType == XSSFCell.CELL_TYPE_BOOLEAN) {// 布尔值
                rowCellData = "";
            } else if (cellType == XSSFCell.CELL_TYPE_ERROR) {// 异常
                rowCellData = "";
            } else {
                throw new Exception(sheetName + " sheet页中" + "第" + rowNo + "行"
                        + "第" + cellNo + "列,单元格格式无法识别，请检查sheet页");
            }
        } catch (Exception e) {
            LogUtility.errorLog("Error when reading", e);
            throw new Exception(sheetName + "sheet页中" + "第" + rowNo + "行" + "第"
                    + cellNo + "列" + "数据不符合要求,请检查sheet页");
        }
        return rowCellData;
    }

    /**
     * 读取第sheetNo个sheet页中第rowNo行的数据
     *
     * @param sheetNo 指定sheetNo页
     * @param rowNo   指定rowNo行
     * @return
     * @throws Exception
     */
    public String[] readExcelByRow(int sheetNo, int rowNo) throws Exception {
        String[] rowData = null;
        XSSFSheet sheet = (XSSFSheet) (getWorkBook().getSheetAt(sheetNo - 1));
        XSSFRow row = sheet.getRow(rowNo - 1);
        int cellCount = row.getLastCellNum();
        rowData = new String[cellCount];
        for (int k = 1; k <= cellCount; k++) {
            try {
                rowData[k - 1] = readExcelByRowAndCell(sheetNo, rowNo, k);
            } catch (Exception e) {
                LogUtility.errorLog("Error when reading", e);
                rowData[k - 1] = "";
            }
        }
        return rowData;
    }

    /**
     * 读取第sheetNo个sheet页中第cellNo列的数据
     *
     * @param sheetNo 指定sheetNo页
     * @param cellNo  指定cellNo列号
     * @return
     * @throws Exception
     */
    public String[] readExcelByCell(int sheetNo, int cellNo) throws Exception {
        String[] cellData = null;
        XSSFSheet sheet = ((XSSFWorkbook) getWorkBook()).getSheetAt(sheetNo - 1);
        int rowCount = sheet.getLastRowNum();
        cellData = new String[rowCount + 1];
        for (int i = 0; i <= rowCount; i++) {
            cellData[i] = readExcelByRowAndCell(sheetNo - 1, i, cellNo - 1);
        }
        return cellData;
    }

    /**
     * 关闭excel工作区，释放资源
     */
    @Override
    public void close() {
        if (_wb != null) {
            try {
                _wb.close();
            } catch (IOException e) {
                LogUtility.errorLog("Error when close", e);
            } finally {
                _wb = null;
            }
        }
    }

    @Override
    public Cell writeExcelByRowAndCell(int sheetNo, int rowNo, int cellNo, boolean value) throws Exception {
        XSSFCell cell = (XSSFCell) getExcelCellByRowAndCell(sheetNo, rowNo, cellNo, true);
        cell.setCellValue(value);

        return cell;
    }

    @Override
    public Cell writeExcelByRowAndCell(int sheetNo, int rowNo, int cellNo, java.util.Calendar value) throws Exception {
        XSSFCell cell = (XSSFCell) getExcelCellByRowAndCell(sheetNo, rowNo, cellNo, true);
        cell.setCellValue(value);

        return cell;
    }

    @Override
    public Cell writeExcelByRowAndCell(int sheetNo, int rowNo, int cellNo, java.util.Date value) throws Exception {
        XSSFCell cell = (XSSFCell) getExcelCellByRowAndCell(sheetNo, rowNo, cellNo, true);
        cell.setCellValue(value);

        return cell;
    }

    @Override
    public Cell writeExcelByRowAndCell(int sheetNo, int rowNo, int cellNo, double value) throws Exception {
        XSSFCell cell = (XSSFCell) getExcelCellByRowAndCell(sheetNo, rowNo, cellNo, true);
        cell.setCellValue(value);

        return cell;
    }

    @Override
    public Cell writeExcelByRowAndCell(int sheetNo, int rowNo, int cellNo, org.apache.poi.ss.usermodel.RichTextString value) throws Exception {
        XSSFCell cell = (XSSFCell) getExcelCellByRowAndCell(sheetNo, rowNo, cellNo, true);
        cell.setCellValue(value);

        return cell;
    }

    @Override
    public Cell writeExcelByRowAndCell(int sheetNo, int rowNo, int cellNo, String value) throws Exception {
        XSSFCell cell = (XSSFCell) getExcelCellByRowAndCell(sheetNo, rowNo, cellNo, true);
        cell.setCellValue(value);

        return cell;
    }

    @Override
    public Cell getExcelCellByRowAndCell(int sheetNo, int rowNo, int cellNo, boolean createIfNotExists) throws Exception {
        XSSFWorkbook wb = (XSSFWorkbook) getWorkBook();
        XSSFSheet sheet = wb.getSheetAt(sheetNo - 1);
        String sheetName = wb.getSheetName(sheetNo - 1);
        try {
            XSSFRow row = sheet.getRow(rowNo - 1);
            if (row == null) {
                if (createIfNotExists) {
                    row = sheet.createRow(rowNo - 1);
                } else {
                    throw new Exception("row not found");
                }
            }
            XSSFCell cell = row.getCell(cellNo - 1);
            if (cell == null) {
                if (createIfNotExists) {
                    cell = row.createCell(cellNo - 1);
                } else {
                    throw new Exception("cell not found");
                }
            }
            return cell;
        } catch (Exception e) {
            LogUtility.errorLog("Error when reading", e);
            throw new Exception(sheetName + "sheet页中" + "第" + rowNo + "行" + "第"
                    + cellNo + "列" + "数据不符合要求,请检查sheet页");
        }
    }

    @Override
    public void write(OutputStream os) throws IOException {
        getWorkBook().write(os);
    }

    @Override
    public Workbook getWorkBook() {
        if (this._wb == null) {
            this._wb = new XSSFWorkbook();
            this._wb.createSheet();
        }

        return this._wb;
    }
}