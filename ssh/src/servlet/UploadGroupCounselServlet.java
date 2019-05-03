package servlet;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import config.Config;
import dao.*;
import dao.impl.*;
import model.*;
import model.role.Staff;
import model.role.Student;
import model.view.CounselorView;
import org.apache.commons.io.FilenameUtils;
import util.AuthCheck;
import util.DateUtils;
import util.FilePartUtils;
import util.MisSystemUtil;
import util.Upload;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Ching Yun Yu on 2018/5/8.
 */

@WebServlet("/UploadGroupCounsel")
@MultipartConfig
public class UploadGroupCounselServlet extends _BaseServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!AuthCheck.isAuth(request, Authority.RoleIndex.COUNSELOR)) {
//        	response.sendRedirect("login");
            request.setAttribute("error", "您無權限使用此系統");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }

        Account account = (Account) request.getSession().getAttribute("account");
        SystemConfigDao systemConfigDao = new SystemConfigDaoImpl(account);
        CadreDao cadredao = new CadreDaoImpl(account);
        CompetitionDao competitiondao = new CompetitionDaoImpl(account);
        LicenseRecordDao licensedao = new LicenseRecordDaoImpl(account);
        VolunteerDao volunteerdao = new VolunteerDaoImpl(account);
        OtherDao otherdao = new OtherDaoImpl(account);

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        int year = 0;
        int sem = 0;
        MisSystemData misSystemData = null;
        try {    // 取得學務系統學年期
            misSystemData = MisSystemUtil.getStuAffairsSbjYS(account.getSchoolSchema());
            year = misSystemData.getSbj_year();
            sem = misSystemData.getSbj_sem();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (action == null) {
            CUnitDao cUnitdao = new CUnitDaoImpl(account);

            List<CUnit> CUnitList = null;
            try {
//                CUnitList = cUnitdao.getCUnit(105, 2, null, null, null);
                CUnitList = cUnitdao.getCUnit(year, sem, null, null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String id = request.getParameter("id");
            if (id != null) {
                try {
                    StudentDao studentDao = new StudentDaoImpl(account);
                    GroupCounselDao groupcounseldao = new GroupCounselDaoImpl(account);
                    GroupCounsel groupCounsel = groupcounseldao.getGroupCounselById(Integer.parseInt(id));
                    request.setAttribute("groupCounsel", groupCounsel);
                    Map<Integer, String> map = new HashMap<>();
                    for (GroupCounselMember groupCounselMember :
                            groupCounsel.getGroupCounselMember()) {
                        Integer rgNo = groupCounselMember.getRegisterNumber();
                        map.put(rgNo, studentDao.getStudentByRgno(rgNo).getStu_Cname());
                    }
                    request.setAttribute("studentNameMap", map);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                GroupCounsel groupCounsel = new GroupCounsel(-1,
                        new Timestamp(new Date().getTime()),
                        new Timestamp(new Date().getTime()),
                        "",
                        "",
                        null,
                        "",
                        "",
                        new ArrayList<>(),
                        "", "", null);
                request.setAttribute("groupCounsel", groupCounsel);
            }

            request.setAttribute("CUnitList", CUnitList);
            request.setAttribute("SelfStaffCode", account.<Staff>getRoleDetail(Authority.RoleIndex.COUNSELOR).getStaffCode());
            request.setAttribute("DateUtils", new DateUtils());
            request.getRequestDispatcher("WEB-INF/jsp/uploadGroupCounsel.jsp").forward(request, response);
        } else if (action.equals("counselor")) {
            AccountDao accountDao = new AccountDaoImpl(account);
            List<Account> accountList;
            List<CounselorView> counselorList = new ArrayList<>();
            try {
                accountList = accountDao.getAccountByRoleIndex(Authority.RoleIndex.COUNSELOR);
                counselorList = accountList.stream()
                        .map(x ->
                                new CounselorView(x.<Staff>getRoleDetail(Authority.RoleIndex.COUNSELOR).getStaffCode(),
                                        x.getName())).collect(Collectors.toList());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Gson gson = new Gson();
            String json = gson.toJson(counselorList);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        } else if (action.equals("classStudentList")) {
            StudentDao studentDao = new StudentDaoImpl(account);
            List<Student> studentList = null;
            String classCode = request.getParameter("class");

            try {
                studentList = studentDao.getStudentListByClass(Integer.toString(year), Integer.toString(sem), classCode);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Map<String, Object> map = new HashMap<>();
            map.put("studentList", studentList);
            Gson gson = new GsonBuilder().addSerializationExclusionStrategy(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                    return !fieldAttributes.getName().matches("className|stu_Cname|regNumber|rgno");
                }

                @Override
                public boolean shouldSkipClass(Class<?> aClass) {
                    return false;
                }
            }).create();
            String json = gson.toJson(map);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        } else if (action.equals("studentList")) {
            StudentDao studentDao = new StudentDaoImpl(account);
            List<Student> studentList = null;
            Integer pageAmount = 1;
            Integer page = 1;
            String clscode = request.getParameter("class");
            String regno = request.getParameter("regno");
            String name = request.getParameter("name");

            try {
//                StudentList = studentDao.getStudentList(regno, "105", "2", clscode, name, null, null, page);
//                pageAmount = studentDao.getPageNumber(regno, "105", "2", clscode, name, null, null, page);
                studentList = studentDao.getStudentList(regno, Integer.toString(year), Integer.toString(sem), clscode, name, null, null, page);
                pageAmount = studentDao.getPageNumber(regno, Integer.toString(year), Integer.toString(sem), clscode, name, null, null, page);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Map<String, Object> map = new HashMap<>();
            map.put("pageAmount", pageAmount);
            map.put("studentList", studentList);
            Gson gson = new GsonBuilder().addSerializationExclusionStrategy(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                    return !fieldAttributes.getName().matches("className|stu_Cname|regNumber|rgno");
                }

                @Override
                public boolean shouldSkipClass(Class<?> aClass) {
                    return false;
                }
            }).create();
            String json = gson.toJson(map);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        if (!AuthCheck.isAuth(request, Authority.RoleIndex.COUNSELOR)) {
            request.setAttribute("error", "您無權限使用此系統");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        UploadFileDao uploadFileDao = new UploadFileDaoImpl(account);
        SystemConfig systemConfig;
        SystemConfigDao systemConfigDao = new SystemConfigDaoImpl(account);
        try {
            systemConfig = systemConfigDao.getSystemConfig();
            if (systemConfig == null)
                throw new NullPointerException();
        } catch (NullPointerException e) {
            e.printStackTrace();
            response.getWriter().write("發生異常錯誤，若有疑問請洽系統管理員");
            return;
            // TODO 警告訊息
        }
        boolean useOldFile = Boolean.parseBoolean(request.getParameter("useOldFile"));

        String id = request.getParameter("id");
        if (id != null && Integer.parseInt(id) != -1) {
            //Update
            Part file = request.getPart("file");
            String submitter = account.<Staff>getRoleDetail(Authority.RoleIndex.COUNSELOR).getStaffCode();

            try {
                GroupCounselDao groupcounseldao = new GroupCounselDaoImpl(account);
                Timestamp startTimestamp = new Timestamp(DateUtils.parseDateTimeInput(request.getParameter("startTime")).getTime());
                Timestamp endTimestamp = new Timestamp(DateUtils.parseDateTimeInput(request.getParameter("endTime")).getTime());
                String title = request.getParameter("title");
                String location = request.getParameter("location");
                String description = request.getParameter("description");
                String counselor = request.getParameter("counselor");
                String[] targets = request.getParameterValues("target[]");
                List<GroupCounselMember> groupCounselMemberList = new ArrayList<>();
                if (targets != null) {
                    for (String target : targets) {
                        GroupCounselMember groupCounselMember = new GroupCounselMember(0, Integer.parseInt(target));
                        groupCounselMemberList.add(groupCounselMember);
                    }
                }
                GroupCounsel oldGroupCounsel = groupcounseldao.getGroupCounselById(Integer.parseInt(id));
                if (file != null && file.getSize() > 0) {
                    //  ------------------------------------ start 上傳檔案 ------------------------------------
//                    randomFileName = upload.uploadFile(account, file, FilenameUtils.getExtension(file.getSubmittedFileName()));
//                    originalFileName = file.getSubmittedFileName();
                    UploadFile uploadFile = uploadFileDao.insert(
                            Config.COUNSEL_UPLOADED_FILE_DIR,
                            systemConfig.getSchoolInfo().getId(),
                            DateUtils.getCurrentSemester(),
                            submitter,
                            FilePartUtils.getSubmittedFileName(file),
                            file.getInputStream());

                    // ------------------------------------ end 上傳檔案 ---------------------------------------
                    GroupCounsel groupCounsel = new GroupCounsel(
                            Integer.parseInt(id), startTimestamp, endTimestamp, title, uploadFile.getFileName(),
                            uploadFile.getId(), location, description, groupCounselMemberList, counselor,
                            submitter, null
                    );
                    groupcounseldao.updateGroupCounsel(groupCounsel);
                    uploadFileDao.deleteById(oldGroupCounsel.getFileId());
                } else {
                    GroupCounsel groupCounsel = new GroupCounsel(
                            Integer.parseInt(id), startTimestamp, endTimestamp, title, (useOldFile) ? oldGroupCounsel.getOriginalFilename() : "",
                            (useOldFile) ? oldGroupCounsel.getFileId() : null,
                            location, description, groupCounselMemberList, counselor, submitter, null
                    );
                    groupcounseldao.updateGroupCounsel(groupCounsel);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //Insert
            try {
                Timestamp startTimestamp = new Timestamp(DateUtils.parseDateTimeInput(request.getParameter("startTime")).getTime());
                Timestamp endTimestamp = new Timestamp(DateUtils.parseDateTimeInput(request.getParameter("endTime")).getTime());
                String title = request.getParameter("title");
                String location = request.getParameter("location");
                String description = request.getParameter("description");
                String counselor = request.getParameter("counselor");
                String[] targets = request.getParameterValues("target[]");
                List<GroupCounselMember> groupCounselMemberList = new ArrayList<>();
                int year = 0;
                int sem = 0;
                MisSystemData misSystemData = null;
                misSystemData = MisSystemUtil.getStuAffairsSbjYS(account.getSchoolSchema());
                StudentDao studentDao = new StudentDaoImpl(account);
                if (targets != null) {
                    year = misSystemData.getSbj_year();
                    sem = misSystemData.getSbj_sem();
                    for (String target : targets) {
                        List<Student> studentList = null;

                        switch (target) {
                            case ("100"):
                                studentList = studentDao.getStudentListByGrade(Integer.toString(year), Integer.toString(sem), "1");
                                for (Student student :
                                        studentList) {
                                    GroupCounselMember groupCounselMember = new GroupCounselMember(0, student.getRgno());
                                    groupCounselMemberList.add(groupCounselMember);
                                }
                                break;
                            case ("200"):
                                studentList = studentDao.getStudentListByGrade(Integer.toString(year), Integer.toString(sem), "2");
                                for (Student student :
                                        studentList) {
                                    GroupCounselMember groupCounselMember = new GroupCounselMember(0, student.getRgno());
                                    groupCounselMemberList.add(groupCounselMember);
                                }
                                break;
                            case ("300"):
                                studentList = studentDao.getStudentListByGrade(Integer.toString(year), Integer.toString(sem), "3");
                                for (Student student :
                                        studentList) {
                                    GroupCounselMember groupCounselMember = new GroupCounselMember(0, student.getRgno());
                                    groupCounselMemberList.add(groupCounselMember);
                                }
                                break;
                            default:
                                GroupCounselMember groupCounselMember = new GroupCounselMember(0, Integer.parseInt(target));
                                groupCounselMemberList.add(groupCounselMember);
                        }

                    }
                }
                Part file = request.getPart("file");
                String submitter = account.<Staff>getRoleDetail(Authority.RoleIndex.COUNSELOR).getStaffCode();

                GroupCounselDao groupcounseldao = new GroupCounselDaoImpl(account);

                if (file != null && file.getSize() > 0) {
                    //  ------------------------------------ start 上傳檔案 ------------------------------------
                    UploadFile uploadFile = uploadFileDao.insert(
                            Config.COUNSEL_UPLOADED_FILE_DIR,
                            systemConfig.getSchoolInfo().getId(),
                            DateUtils.getCurrentSemester(),
                            submitter,
                            FilePartUtils.getSubmittedFileName(file),
                            file.getInputStream());


                    // ------------------------------------ end 上傳檔案 ---------------------------------------
                    GroupCounsel groupCounsel = new GroupCounsel(-1,
                            new Timestamp(new Date().getTime()),
                            new Timestamp(new Date().getTime()),
                            "",
                            null,
                            null,
                            "",
                            "",
                            new ArrayList<>(),
                            "", "", null);
                    groupCounsel.setStartTime(startTimestamp);
                    groupCounsel.setEndTime(endTimestamp);
                    groupCounsel.setTitle(title);
                    groupCounsel.setFileId(uploadFile.getId());
                    groupCounsel.setOriginalFilename(uploadFile.getFileName());
                    groupCounsel.setLocation(location);
                    groupCounsel.setDescription(description);
                    groupCounsel.setGroupCounselMember(groupCounselMemberList);
                    groupCounsel.setCounselor(counselor);
                    groupCounsel.setSubmitter(submitter);
                    groupcounseldao.insertGroupCounsel(groupCounsel);
                }
                else {
                    GroupCounsel groupCounsel = new GroupCounsel(-1,
                            new Timestamp(new Date().getTime()),
                            new Timestamp(new Date().getTime()),
                            "",
                            null,
                            null,
                            "",
                            "",
                            new ArrayList<>(),
                            "", "", null);
                    groupCounsel.setStartTime(startTimestamp);
                    groupCounsel.setEndTime(endTimestamp);
                    groupCounsel.setTitle(title);
                    groupCounsel.setFileId(null);
                    groupCounsel.setOriginalFilename("");
                    groupCounsel.setLocation(location);
                    groupCounsel.setDescription(description);
                    groupCounsel.setGroupCounselMember(groupCounselMemberList);
                    groupCounsel.setCounselor(counselor);
                    groupCounsel.setSubmitter(submitter);
                    groupcounseldao.insertGroupCounsel(groupCounsel);
                }


            } catch (NullPointerException e) {
                e.printStackTrace();
                request.setAttribute("error", "參數設定錯誤，若有疑問請洽系統管理員。");
                request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
                return;
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "系統發生錯誤，若有疑問請洽系統管理員。");
                request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
                return;
            }
        }
        response.sendRedirect("GroupCounsel");

    }

}
