package servlet;

//import com.mysql.cj.x.protobuf.MysqlxDatatypes;

import dao.*;
import dao.impl.*;
import model.*;
import model.role.Student;
import util.AuthCheck;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by David on 2018/1/29.
 */

@WebServlet("/courseRecordUnlock")
@MultipartConfig
public class CourseRecordUnlockServlet extends _BaseServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        if (!AuthCheck.isAuth(request, Authority.RoleIndex.STUDENT)) {
//        	response.sendRedirect("login");
        	request.setAttribute("error", "您無權限使用此系統");
        	request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        try {
            HttpSession session = request.getSession();
            Account account = (Account) session.getAttribute("account");
            CourseRecordDao courseRecordDao = new CourseRecordDaoImpl(account);
            //TODO 更好的取得RGNO
            Integer rgno  =Integer.valueOf(((Student)account.getRoleDetail(Authority.RoleIndex.STUDENT)).getRgno());
            String id = request.getParameter("id");
            CourseRecord courseRecord = courseRecordDao.getCourseRecordByID(rgno,id);
            courseRecord.setStatus("3");
            courseRecord.setCheck("0");
            courseRecordDao.updateCourseRecordByID(rgno,id,courseRecord);

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
}
