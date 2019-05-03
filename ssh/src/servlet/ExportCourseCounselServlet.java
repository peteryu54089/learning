package servlet;

import dao.GroupCounselDao;
import dao.IndividualCounselDao;
import dao.impl.GroupCounselDaoImpl;
import dao.impl.IndividualCounselDaoImpl;
import model.Account;
import model.GroupCounsel;
import model.IndividualCounsel;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by Ching Yun Yu on 2018/5/9.
 */

@WebServlet("/ExportCourseCounsel")
public class ExportCourseCounselServlet extends _BaseServlet {


    @SuppressWarnings({"unchecked"})
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();

        String outputFile = "file/" + (new Date()).getTime() + "課程諮詢報表.xlsx";

        try (HSSFWorkbook workbook = new HSSFWorkbook()) {
            List<IndividualCounsel> individualCounselList = (List<IndividualCounsel>) session.getAttribute("individualCounselList");
            List<GroupCounsel> groupCounselList = (List<GroupCounsel>) session.getAttribute("groupCounselList");
            HSSFSheet sheet = workbook.createSheet();
            HSSFRow row = sheet.createRow((short) 0);
            HSSFCell cell = row.createCell((short) 0);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            if (individualCounselList.size() > 0) {
                cell.setCellValue("個別諮詢管理");
                row = sheet.createRow((short) 1);
                cell = row.createCell((short) 0);
                cell.setCellValue("label1");
                cell = row.createCell((short) 1);
                cell.setCellValue("label2");
                cell = row.createCell((short) 2);
                cell.setCellValue("label3");
                cell = row.createCell((short) 3);
                cell.setCellValue("label4");
                cell = row.createCell((short) 4);
                cell.setCellValue("label5");
            }
            for (int i = 0; i < individualCounselList.size(); i++) {
//                row = sheet.createRow((short) (i + 2));
//                cell = row.createCell((short) 0);
//                cell.setCellValue(individualCounselList.get(i).getLabel1());
//                cell = row.createCell((short) 1);
//                cell.setCellValue(individualCounselList.get(i).getLabel2());
//                cell = row.createCell((short) 2);
//                cell.setCellValue(individualCounselList.get(i).getLabel3());
//                cell = row.createCell((short) 3);
//                cell.setCellValue(individualCounselList.get(i).getLabel4());
//                cell = row.createCell((short) 4);
//                cell.setCellValue(individualCounselList.get(i).getLabel5());
            }
            if (groupCounselList.size() > 0) {
                row = sheet.createRow((short) (individualCounselList.size() + 2));
                cell = row.createCell((short) 0);
                cell.setCellValue("團體輔導管理");
                row = sheet.createRow((short) (individualCounselList.size() + 3));
                cell = row.createCell((short) 0);
                cell.setCellValue("開始日期");
                cell = row.createCell((short) 1);
                cell.setCellValue("結束日期");
//                cell = row.createCell((short) 2);
//                cell.setCellValue("人數");
                cell = row.createCell((short) 4);
                cell.setCellValue("主題");
                cell = row.createCell((short) 5);
                cell.setCellValue("內容敘述");
                cell = row.createCell((short) 6);
                cell.setCellValue("檔案");
            }
            for (int i = 0; i < groupCounselList.size(); i++) {
                row = sheet.createRow((short) (i + individualCounselList.size() + 4));
                cell = row.createCell((short) 0);
                cell.setCellValue(groupCounselList.get(i).getStartTime());
                cell = row.createCell((short) 1);
                cell.setCellValue(groupCounselList.get(i).getEndTime());
//                cell = row.createCell((short) 3);
//                cell.setCellValue(groupCounselList.get(i).getTotal());
                cell = row.createCell((short) 4);
                cell.setCellValue(groupCounselList.get(i).getTitle());
                cell = row.createCell((short) 5);
                cell.setCellValue(groupCounselList.get(i).getDescription());
                cell = row.createCell((short) 6);
                cell.setCellValue(groupCounselList.get(i).getOriginalFilename());
            }
            FileOutputStream fOut = new FileOutputStream(outputFile);
            workbook.write(fOut);
            fOut.flush();
            fOut.close();
            System.out.println("檔生成...");
            System.out.println("groupCounselList:" + groupCounselList.size());
            System.out.println("individualCounselList:" + individualCounselList.size());
        } catch (Exception e) {
            System.out.println("已運行 xlCreate() : " + e);
        }
        response.sendRedirect("CourseCounsel");

    }

}
