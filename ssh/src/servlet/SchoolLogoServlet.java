package servlet;

import dao.SystemConfigDao;
import dao.impl.SystemConfigDaoImpl;
import model.HasSchoolSchema;
import util.HttpUtils;
import util.Upload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@WebServlet("/schoolLogo")
public class SchoolLogoServlet extends _BaseServlet {
    public static final String PATH = "logo";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        File file = null;
        try {
            HttpSession session = request.getSession();
            SystemConfigDao dao = new SystemConfigDaoImpl((HasSchoolSchema) session.getAttribute("account"));
            String logoPath = dao.getSystemConfig().getSchoolInfo().getLogoPath();
            if (logoPath != null) {
                file = Paths.get(Upload.getSaveRootDir(getServletContext()).getAbsolutePath(), PATH, logoPath).toFile();
                if (!file.exists())
                    file = null;
            }

            if (file == null) {
                file = Paths.get(
                        Upload.getSaveRootDir(getServletContext()).getAbsolutePath(),
                        PATH,
                        "Taipeitech.jpg").toFile();
            }

            response.setHeader("Cache-Control", "max-age=60");
            HttpUtils.sendFile(response, file);
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendError(404);
    }
}
