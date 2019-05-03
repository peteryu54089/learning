package servlet.management;

import model.Account;
import model.Authority;
import util.MenuBarUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@WebServlet("/showStudentPerformance")
@MultipartConfig
public class ShowStudentPerformanceServlet extends BaseManageServlet {
    private Set<String> pageWhiteList = new HashSet<>(Arrays.asList("cadreRecordViewCadreRecord", "cadre", "competition", "license", "volunteer", "other"));
    private Map<String, String> pageDescMap = new HashMap<>();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        if (checkPermission(request, response)==null)
            return;

        bindStuClsInfo(request, response);

        if(pageDescMap .size()!=pageWhiteList.size()){
            pageDescMap.put("cadreRecordViewCadreRecord", "校內幹部經歷紀錄");
            pageDescMap.put("cadre", "幹部經歷紀錄");
            pageDescMap.put("competition", "競賽參與紀錄");
            pageDescMap.put("license", "檢定證照紀錄");
            pageDescMap.put("volunteer", "志工服務紀錄");
            pageDescMap.put("other", "其他活動紀錄");
        }

        String pageName = request.getParameter("p");
        if (pageName == null || !pageWhiteList.contains(pageName))
            pageName = "cadre";

        request.setAttribute("pageName", pageName);
        request.setAttribute("title", "查詢學生多元表現 - " + (pageDescMap.getOrDefault(pageName, "")));
        request.setAttribute("MenuBarUtil", new MenuBarUtil());
        request.getRequestDispatcher("/WEB-INF/jsp/managerPage/studentPerformance.jsp").forward(request, response);
    }

    protected Account checkPermission(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        if (account == null || !account.getAuthority()
                .containsRole(
                        Authority.RoleIndex.MANAGER,
                        Authority.RoleIndex.WORK_TEAM
                )
        ) {
            response.setStatus(401);
            return null;
        }

        return account;
    }
}
