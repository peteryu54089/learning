package servlet;

import dao.SystemConfigDao;
import dao.impl.SystemConfigDaoImpl;
import model.Account;
import model.Authority;
import model.HasSchoolSchema;
import model.SystemConfig;
import util.HttpUtils;
import util.Upload;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@WebServlet("/schoolBanner")
@MultipartConfig
public class SchoolBannerServlet extends _BaseServlet {
    public static final String PATH = "banner";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        File file = null;
        try {
            HttpSession session = request.getSession();
            SystemConfigDao dao = new SystemConfigDaoImpl((HasSchoolSchema) session.getAttribute("account"));
            String bannerPath = (String) session.getAttribute("bannerPath");
            if (bannerPath == null) {
                // DO CACHE
                bannerPath = dao.getSystemConfig().getSchoolInfo().getBannerPath();
                session.setAttribute("bannerPath", bannerPath);
            }
            if (bannerPath != null) {
                file = Paths.get(Upload.getSaveRootDir(getServletContext()).getAbsolutePath(), PATH, bannerPath).toFile();
                if (!file.exists())
                    file = null;
            }

            if (file == null) {
                file = Paths.get(
                        Upload.getSaveRootDir(getServletContext()).getAbsolutePath(),
                        PATH,
                        "default_banner.jpg").toFile();
            }

            response.setHeader("Cache-Control", "max-age=60");
            HttpUtils.sendFile(response, file);
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendError(404);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        if (account == null || !account.getAuthority().checkRole(Authority.RoleIndex.MANAGER)) {
            response.setStatus(401);
            return;
        }

        try {
            SystemConfigDao dao = new SystemConfigDaoImpl(account);
            SystemConfig config = dao.getSystemConfig();
            Upload upload = new Upload(getServletContext());
            Part bannerFile = request.getPart("banner");
            if (bannerFile != null && bannerFile.getSize() > 0) {
                String fileName = upload.uploadFile(
                        PATH, bannerFile
                );

                config.getSchoolInfo().setBannerPath(fileName);
                dao.saveSystemConfig(config);
                session.removeAttribute("bannerPath");
                response.setStatus(200);
            } else {
                response.setStatus(304);
            }

        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
            response.setStatus(500);
        }
    }
}
