package servlet;

import dao.*;
import dao.impl.*;
import model.*;
import model.role.Student;
import util.Upload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/tutorViewCourseRecord")
public class TutorViewCourseRecordServlet extends _BaseServlet {
    //TODO 頁數整合
    public final static Integer RECORD_PER_PAGE = 4;


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        StudentDao studentDao = new StudentDaoImpl(account);
        SelectCourseDao selectCourseDao = new SelectCourseDaoImpl(account);
        CourseRecordDao courseRecordDao = new CourseRecordDaoImpl(account);
        SystemConfigDao systemConfigDao = new SystemConfigDaoImpl(account);
        SystemConfig systemConfig = systemConfigDao.getSystemConfig();
        Integer rgno = Integer.valueOf(request.getParameter("rgno"));
        Student student = studentDao.getStudentByRgno(rgno);

        try {
            Integer page = 1;
            if (request.getParameter("page") != null) {
                page = Integer.parseInt(request.getParameter("page"));
            }
            String year = request.getParameter("year");
            String semester = request.getParameter("semester");
            String name = request.getParameter("courseName");
            String verifystate = request.getParameter("verifystate");
            List<Course> courseList;
            if (verifystate != null) {
                courseList = selectCourseDao.getSelectCourse(student.getRgno(), year, semester, name, null);
            } else {
                courseList = selectCourseDao.getSelectCourse(student.getRgno(), year, semester, name, page);
            }

            List<CourseRecord> allCourseRecords = courseRecordDao.getCourseRecord(rgno, null, null,
                    courseList.stream().map(Course::getCourse_no).collect(Collectors.toList()),
                    null, verifystate, null, false, null, null);

            Map<String, Course> courseListMap = new HashMap<>();


            for (Course c : courseList) {
                courseListMap.put(c.getCourse_no(), c);
                c.setCourseRecordList(new ArrayList<>());
            }


            CourseRecordDocumentDao courseRecordDocumentDao = new CourseRecordDocumentDaoImpl(account);
            Map<String, CourseRecord> courseRecordMap = new HashMap<>();
            for (CourseRecord courseRecord : allCourseRecords) {
                String cno = courseRecord.getCourse_num();
                Course course = courseListMap.get(cno);
                if (course != null) {
                    courseRecord.setCourse(course);
                    courseRecord.setCourseRecordDocumentList(new ArrayList<>());
                    course.getCourseRecordList().add(courseRecord);
                    courseRecordMap.put(courseRecord.getId(), courseRecord);
                }
            }

            List<CourseRecordDocument> courseRecordDocumentList = courseRecordDocumentDao.getCourseRecordDocuments(
                    allCourseRecords.stream().map(CourseRecord::getId).collect(Collectors.toList())
            );

            if (courseRecordDocumentList != null) {
                for (CourseRecordDocument recordDocument : courseRecordDocumentList) {
                    CourseRecord courseRecord = courseRecordMap.get(recordDocument.getCrid().toString());
                    if (courseRecord != null) {
                        courseRecord.getCourseRecordDocumentList().add(recordDocument);
                    }

                    recordDocument.setDlLink(Upload.generateDownloadLink(request, account, recordDocument.getUploadFile()));
                }
            }

            if (verifystate != null)
                courseList = courseList.stream().filter(x -> x.getCourseRecordList().size() != 0).collect(Collectors.toList());

            session.setAttribute("allowType", systemConfig.getCourseStudyRecord().getAllowTypesString());
            session.setAttribute("size", systemConfig.getCourseStudyRecord().getMaxSize());
            Integer pageAmount;
            if (verifystate != null)
                pageAmount = ((courseList.size() - 1) / RECORD_PER_PAGE) + 1;
            else
                pageAmount = selectCourseDao.getPageNumber(student.getRgno(), year, semester, name);
            if (verifystate != null)
                courseList = courseList.subList((page - 1) * RECORD_PER_PAGE, Math.min((page) * RECORD_PER_PAGE, courseList.size()));
            if (courseList.size() == 0 && page != 1) {
                page = page - 1;
                String url = "courseRecord?page=" + page;
                if (year != null)
                    url += "&year=" + year;
                if (semester != null)
                    url += "&semester=" + semester;
                if (name != null)
                    url += "&courseName=" + name;
                if (verifystate != null)
                    url += "&verifystate=" + verifystate;
                response.sendRedirect(url);
            }

            request.setAttribute("fileAmount", systemConfig.getCourseStudyRecord().getMaxAmountPerRecord());
            request.setAttribute("year", year);
            request.setAttribute("rgno", rgno);
            request.setAttribute("semester", semester);
            request.setAttribute("courseName", name);
            request.setAttribute("page", page);
            request.setAttribute("student", student);
            request.setAttribute("pageAmount", pageAmount);
            request.setAttribute("courseList", courseList);
            request.setAttribute("verifystate", verifystate);
            request.getRequestDispatcher("/WEB-INF/jsp/tutorViewCourseRecord.jsp").forward(request, response);

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
}

