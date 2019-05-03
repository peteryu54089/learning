package servlet;

import dao.CadreDao;
import dao.impl.CadreDaoImpl;
import model.Account;
import model.Authority;
import util.AuthCheck;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/cadreRecordImport")
@MultipartConfig
public class CadreRecordImportServlet extends _BaseServlet {
    private static final String IMPORT_RESULT_KEY = "_cadreImportResult";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!AuthCheck.isAuth(request)) {
            request.setAttribute("error", "您無權限使用此系統");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");

        if (!account.getAuthority().containsRole(
                Authority.RoleIndex.MANAGER, Authority.RoleIndex.WORK_TEAM, Authority.RoleIndex.CADRE_SUBMITTER
        )) {
            response.setStatus(401);
            return;
        }

        List<String> errorMsg = new ArrayList<>();
        Part file = request.getPart("import");
        InputStream is = null;
        if (file == null || file.getSize() <= 0) {
            errorMsg.add("請上傳檔案");
        } else {
            is = file.getInputStream();
        }

        CadreDao.ImportResult importResult;
        if (is != null) {
            CadreDao dao = new CadreDaoImpl(account);
            importResult = dao.importFromExcelInputStream(file.getSubmittedFileName(), is);
        } else {
            importResult = new CadreDao.ImportResult();
            importResult.errors = errorMsg;
        }

        session.setAttribute(IMPORT_RESULT_KEY, importResult);
        response.sendRedirect("./cadreRecordImport");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!AuthCheck.isAuth(request)) {
            request.setAttribute("error", "您無權限使用此系統");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");

        if (!account.getAuthority().containsRole(Authority.RoleIndex.MANAGER, Authority.RoleIndex.WORK_TEAM, Authority.RoleIndex.CADRE_SUBMITTER)) {
            response.setStatus(401);
            return;
        }

        CadreDao.ImportResult importResult;

        try {
            importResult = (CadreDao.ImportResult) session.getAttribute(IMPORT_RESULT_KEY);
            if (importResult == null) {
                importResult = new CadreDao.ImportResult();
            }
        } catch (Exception e) {
            importResult = new CadreDao.ImportResult();
        }

        session.removeAttribute(IMPORT_RESULT_KEY);

        request.setAttribute(IMPORT_RESULT_KEY, importResult);
        request.getRequestDispatcher("/WEB-INF/jsp/cadreRecordImport.jsp").forward(request, response);
    }
}
