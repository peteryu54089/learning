package servlet.management;

import dao.impl.SystemConfigDaoImpl;
import model.Account;
import model.SystemConfig;
import servlet.SchoolLogoServlet;
import util.StringUtils;
import util.Upload;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;

@WebServlet("/schoolBasicInfoMange")
@MultipartConfig
public class SchoolBasicManageServlet extends BaseManageServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        Account account;
        if ((account = checkPermission(request, response))==null)
            return;

        SystemConfigDaoImpl systemConfigDao = new SystemConfigDaoImpl(account);

        SystemConfig config = systemConfigDao.getSystemConfig();
        request.setAttribute("config", config);

        request.getRequestDispatcher("/WEB-INF/jsp/managerPage/manageSchoolBasicInfo.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        Account account;
        if ((account = checkPermission(request, response))==null)
            return;

        SystemConfigDaoImpl systemConfigDao = new SystemConfigDaoImpl(account);

        try {
            SystemConfig config = systemConfigDao.getSystemConfig();
            SystemConfig.SchoolInfo schoolInfo = config.getSchoolInfo();
            String name = request.getParameter("name");
            String address = request.getParameter("address");
            String phone = request.getParameter("phone");
            String website = request.getParameter("website");
            Part logo = request.getPart("logo");

            if (!StringUtils.isNullOrEmpty(name))
                schoolInfo.setName(name);
            if (!StringUtils.isNullOrEmpty(address))
                schoolInfo.setAddress(address);
            if (!StringUtils.isNullOrEmpty(phone))
                schoolInfo.setPhone(phone);
            if (!StringUtils.isNullOrEmpty(website))
                schoolInfo.setWebsite(website);

            if (logo != null && logo.getSize() > 0) {
                Upload upload = new Upload(getServletContext());
                String fileName = upload.uploadFile(SchoolLogoServlet.PATH, logo);
                schoolInfo.setLogoPath(fileName);
            }

            systemConfigDao.saveSystemConfig(config);
            response.setStatus(200);
        } catch (NullPointerException e) {
            e.printStackTrace();
            response.setStatus(500);
        }
    }
}
