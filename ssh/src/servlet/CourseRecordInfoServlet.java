package servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.CourseRecordDao;
import dao.impl.CourseRecordDaoImpl;
import model.Account;
import model.CourseRecord;
import model.CourseRecordDocument;
import util.Upload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/courseRecordInfo")

public class CourseRecordInfoServlet extends _BaseServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");

        CourseRecordDao courseRecordDao = new CourseRecordDaoImpl(account);
        String id = request.getParameter("id");
        try {
            CourseRecord courseRecord = courseRecordDao.getCourseRecordByID(null, id);
            for (CourseRecordDocument recordDocument :courseRecord.getCourseRecordDocumentList()) {
                recordDocument.setDlLink(Upload.generateDownloadLink(request, account, recordDocument.getUploadFile()));
            }
            List<String> documentname = new ArrayList<>();
            List<String> documentid = new ArrayList<>();
            List<String> documentlink = new ArrayList<>();

            for (CourseRecordDocument crd : courseRecord.getCourseRecordDocumentList()) {

                documentname.add(crd.getOriginal_filename());
                documentid.add(Integer.toString(crd.getUpload_file_id()));
                documentlink.add(crd.getDlLink());
            }
            Map<String, String> options = new LinkedHashMap<>();
            options.put("staff", courseRecord.getVerifier_staff_code());
            options.put("submittedDate", courseRecord.getSubmittedDateString());
            options.put("verifyDate", courseRecord.getVerifyDateString());
            options.put("student", courseRecord.getStu_name());
            options.put("content", courseRecord.getContent());
            options.put("verify_message", courseRecord.getVerify_message());
            options.put("status", courseRecord.getStatus());
            options.put("documentname", documentname.toString());
            options.put("documentid", documentid.toString());
            options.put("dlink", documentlink.toString());
            options.put("statusC", courseRecord.getStatusC());
            options.put("id", courseRecord.getId());
            Gson gson = new GsonBuilder().create();
            String json = gson.toJson(options);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);

        } catch (NullPointerException e) {
            e.printStackTrace();
            response.getWriter().write("發生異常錯誤，若有疑問請洽系統管理員");
            return;
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("發生異常錯誤，若有疑問請洽系統管理員");
            return;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
