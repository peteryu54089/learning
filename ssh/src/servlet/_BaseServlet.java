package servlet;

import dao.impl.UploadFileDaoImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class _BaseServlet extends HttpServlet {
    protected _BaseServlet() {
    }

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UploadFileDaoImpl.setWebInfPath(getServletContext().getRealPath("WEB-INF"));
        super.service(request, response);
    }
}
