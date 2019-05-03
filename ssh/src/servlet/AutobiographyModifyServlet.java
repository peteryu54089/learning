package servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.AutobiographyDao;
import dao.impl.AutobiographyDaoImpl;
import model.Account;
import model.Authority;
import model.Autobiography;
import model.role.Student;
import util.AuthCheck;
import util.Upload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet("/autobiographymodify")

public class AutobiographyModifyServlet extends _BaseServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        if (!AuthCheck.isAuth(request, Authority.RoleIndex.STUDENT)) {
//        	response.sendRedirect("login");
        	request.setAttribute("error", "您無權限使用此系統");
        	request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        //TODO 更好的取得RGNO
        Integer rgno = ((Student) account.getRoleDetail(Authority.RoleIndex.STUDENT)).getRgno();

        String id = request.getParameter("id");
        try {
            AutobiographyDao autobiographydao = new AutobiographyDaoImpl(account);
            Autobiography autobiography = autobiographydao.getAutobiographyByID(rgno, id);
            Map<String, String> options = new LinkedHashMap<>();
            options.put("id", autobiography.getId().toString());
            options.put("topic", autobiography.getTopic());
            options.put("description", autobiography.getDescription());
            options.put("main_file_name", autobiography.getMain_uploadFile().getFileName());

            options.put("main_file_link", Upload.generateDownloadLink(request, account, autobiography.getMain_uploadFile()));
            if(autobiography.getSub_file_id()!=null && autobiography.getSub_file_id()!=0){
                options.put("sub_file_name", autobiography.getSub_uploadFile().getFileName());
                options.put("sub_file_link", Upload.generateDownloadLink(request, account, autobiography.getSub_uploadFile()));
            }

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
