package servlet;

import dao.impl.UploadFileDaoImpl;
import model.DownloadFileRequest;
import util.HttpUtils;
import util.Upload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/externalDownloadLink")
public class ExternalDownloadLinkServlet extends _BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            DownloadFileRequest model = null;
            String token = request.getParameter("d");
            if (token != null && (model = Upload.decodeDownloadLink(token)) != null) {
                if (model.getIdNo() == null) {
                    File file = new File(
                            UploadFileDaoImpl.getWebInfoPath(),
                            model.getFsName()
                    );

                    if (file.exists()) {
                        HttpUtils.sendFile(response, file, model.getOriginalName());
                        return;
                    }
                } else {
                    // old code
                    Upload.getSaveRootDir(getServletContext());

                    File file = new File(
                            Upload.getAccountBaseDir(model.getIdNo()),
                            new File(model.getFsName()).getName());

                    if (file.exists()) {
                        HttpUtils.sendFile(response, file, model.getOriginalName());
                        return;
                    }
                }
            }

            response.setStatus(404);
            response.getWriter().println("404 Not Found");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(400);
            response.getWriter().println("400 Bad Request");
        }
    }
}
