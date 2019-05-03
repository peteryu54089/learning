package servlet;

import dao.*;
import dao.impl.*;
import model.Account;
import model.GroupCounsel;
import model.IndividualCounsel;
import util.AuthCheck;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Ching Yun Yu on 2018/6/13.
 */

@WebServlet("/CourseCounsel")
@MultipartConfig
public class CourseCounselServlet extends _BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (!AuthCheck.isAuth(request)) {
//        	response.sendRedirect("login");
        	request.setAttribute("error", "您無權限使用此系統");
        	request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");

        CadreDao cadredao = new CadreDaoImpl(account);
        CompetitionDao competitiondao = new CompetitionDaoImpl(account);
        LicenseRecordDao licensedao = new LicenseRecordDaoImpl(account);
        VolunteerDao volunteerdao = new VolunteerDaoImpl(account);
        OtherDao otherdao = new OtherDaoImpl(account);
        //IndividualCounselDao individualcounseldao = new IndividualCounselDaoImpl(account);
        GroupCounselDao groupcounseldao = new GroupCounselDaoImpl(account);
        IndividualCounsel individualCounsel = (IndividualCounsel) session.getAttribute("individualCounsel");
        GroupCounsel groupCounsel = (GroupCounsel) session.getAttribute("groupCounsel");

        try {
//            List<IndividualCounsel> individualCounselList = individualcounseldao.getIndividualCounselByText1AndText2(individualCounsel.getText1(), individualCounsel.getText2());
//            session.setAttribute("individualCounselList", individualCounselList);
            List<GroupCounsel> groupCounselList = groupcounseldao.getGroupCounselByTitleOrDate(groupCounsel.getStartTime(), groupCounsel.getEndTime(), groupCounsel.getTitle());
            session.setAttribute("groupCounselList", groupCounselList);
            request.getRequestDispatcher("/WEB-INF/jsp/courseCounsel.jsp").forward(request, response);
            return;
        } catch (NumberFormatException e) {
            request.setAttribute("error", "參數設定錯誤，若有疑問請洽系統管理員。");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "發生異常錯誤，請洽系統管理員");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        if (!AuthCheck.isAuth(request)) {
//        	response.sendRedirect("login");
        	request.setAttribute("error", "您無權限使用此系統");
        	request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();

        try {
//            IndividualCounsel individualCounsel = new IndividualCounsel(
//                    null,
//                    null,
//                    null,
//                    null,
//                    null,
//                    null,
//                    request.getParameter("startDate"),
//                    request.getParameter("topic"),
//                    null
//            );
//            session.setAttribute("individualCounsel", individualCounsel);
            GroupCounsel groupCounsel = new GroupCounsel(
                    0,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            session.setAttribute("groupCounsel", groupCounsel);
        } catch (NullPointerException e) {
            e.printStackTrace();
            request.setAttribute("error", "參數設定錯誤，若有疑問請洽系統管理員。");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "系統發生錯誤，若有疑問請洽系統管理員。");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        response.sendRedirect("CourseCounsel");
    }

}
