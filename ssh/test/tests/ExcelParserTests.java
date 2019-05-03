package tests;

import org.junit.Assert;
import org.junit.Test;
import util.excel.ExcelParse2007;
import util.excel.IExcelParse;

import java.io.InputStream;
import java.util.Date;

public class ExcelParserTests {
    @Test
    public void testReading() {
        try (IExcelParse parse = new ExcelParse2007()) {
            try (InputStream is = ExcelParserTests.class.getResourceAsStream("../EXCEL.xlsx")) {
                parse.loadExcel(is);
                int rowNo = 0;
                Assert.assertEquals(
                        123.0,
                        parse.getExcelCellByRowAndCell(1, ++rowNo, 1, false).getNumericCellValue(),
                        0.01);

                Assert.assertEquals(new Date(2015 - 1900,0,1),
                        parse.getExcelCellByRowAndCell(1, ++rowNo, 1, false).getDateCellValue());

                Assert.assertEquals("abc",
                        parse.getExcelCellByRowAndCell(1, ++rowNo, 1, false).getStringCellValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

}
