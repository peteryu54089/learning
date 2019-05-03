package servlet;

import dao.CourseRecordDocumentDao;
import dao.impl.CourseRecordDocumentDaoImpl;
import model.CourseRecordDocument;
import model.DownloadFileRequest;
import util.ObjectUtils;
import util.Upload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/externalCourseRecordView")
public class ExternalCourseRecordViewServlet extends _BaseServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DownloadFileRequest model = null;
        String token = request.getParameter("d");
        if (token != null && (model = Upload.decodeDownloadLink(token)) != null) {
            String schema = model.getTable();
            CourseRecordDocumentDao dao = new CourseRecordDocumentDaoImpl(() -> schema);
            List<Object> list;
            try {
                list = dao.getCourseRecordDocumentWithIdno(String.valueOf(model.getId())).stream()
                        .map(x -> new Object() {
                            public final String link = Upload.generateDownloadLink(request, new DownloadFileRequest(
                                            "CourseRecordDocument",
                                            x.getUpload_file_id(),
                                            ObjectUtils.getField(x, "idno"),
                                            x.getDocument(),
                                            x.getOriginal_filename()
                                    )
                            );
                            public final CourseRecordDocument doc = x;
                        })
                        .collect(Collectors.toList());
            } catch (SQLException e) {
                e.printStackTrace();
                list = Collections.emptyList();
            }
            request.setAttribute("list", list);

            request.getRequestDispatcher("/WEB-INF/jsp/externalCourseRecordView.jsp").forward(request, response);
        } else {
            response.setStatus(404);
        }

    }
}
