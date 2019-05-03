package servlet;

import dao.ProfitDao;
import dao.SystemConfigDao;
import dao.impl.ProfitDaoImpl;
import dao.impl.SystemConfigDaoImpl;
import model.Account;
import model.Authority;
import model.Profit;
import model.SystemConfig;
import model.role.*;
import util.DateUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by David on 2018/1/29.
 */
@WebServlet("/main")
public class MainServlet extends _BaseServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            request.setAttribute("error", "您無權限使用此系統");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
        SystemConfigDao systemConfigDao = new SystemConfigDaoImpl(account);
        SystemConfig systemConfig = systemConfigDao.getSystemConfig();

        List<Authority.RoleIndex> roles = account.getAuthority().getCurrentRoles();
        if (roles.size() > 1 && session.getAttribute("selectedRoleIndex") == null) {
            request.setAttribute("account", account);
            request.setAttribute("roles", roles);
            request.getRequestDispatcher("/WEB-INF/jsp/mainPage/selectRoles.jsp").forward(request, response);

            return;
        } else if (roles.size() == 1) {
            session.setAttribute("selectedRoleIndex", roles.get(0));
        }

        session.setAttribute("selectedRole", getRoleDetails(request, (Authority.RoleIndex) session.getAttribute("selectedRoleIndex")));
        try {
            {
                Manager manager = getRoleDetails(request, Authority.RoleIndex.MANAGER);
                if (manager != null) {
                    request.getRequestDispatcher("/WEB-INF/jsp/mainPage/managerMain.jsp").forward(request, response);
                    return;
                }
            }

            {
                Student student = (Student) getRoleDetails(request, Authority.RoleIndex.STUDENT);
                if (student != null) {
                    request.setAttribute("config", systemConfig);
                    request.setAttribute("DateUtils", new DateUtils());

                    request.setAttribute("student", student);
                    request.getRequestDispatcher("/WEB-INF/jsp/mainPage/studentMain.jsp").forward(request, response);
                    return;
                }
            }

            {
                Tutor tutor = (Tutor) getRoleDetails(request, Authority.RoleIndex.TUTOR);
                if (tutor != null) {
                    request.getRequestDispatcher("/WEB-INF/jsp/mainPage/tutorMain.jsp").forward(request, response);
                    return;
                }
            }

            {
                Teacher teacher = (Teacher) getRoleDetails(request, Authority.RoleIndex.TEACHER);
                if (teacher != null) {
                    ProfitDao profitdao = new ProfitDaoImpl(account);
                    List<Profit> profitList;
                    final Date now = new Date();

                    if (systemConfig.getCourseStudyRecord().getTeacherStartDateTime().after(now)
                            || systemConfig.getCourseStudyRecord().getTeacherEndDateTime().before(now)
                    ) {
                        profitList = null;
                    } else {
                        profitList = profitdao.getProfit(
                                systemConfig.getCourseStudyRecord().getActiveYear(),
                                systemConfig.getCourseStudyRecord().getActiveSem(),
                                teacher.getStaffCode()
                        );
                    }

                    request.setAttribute("config", systemConfig);
                    request.setAttribute("allowVerifyTimeRange", DateUtils.joinDate(
                            systemConfig.getCourseStudyRecord().getTeacherStartDateTime(),
                            systemConfig.getCourseStudyRecord().getTeacherEndDateTime(), " ~ ")
                    );
                    request.setAttribute("profitList", profitList);
                    request.getRequestDispatcher("/WEB-INF/jsp/mainPage/teacherMain.jsp").forward(request, response);
                    return;
                }
            }

            {
                Counselor counselor = (Counselor) getRoleDetails(request, Authority.RoleIndex.COUNSELOR);
                if (counselor != null) {
                    request.getRequestDispatcher("/WEB-INF/jsp/mainPage/counselorMain.jsp").forward(request, response);
                    return;
                }
            }

            {
                CourseSubmitter courseSubmitter = (CourseSubmitter) getRoleDetails(request, Authority.RoleIndex.COURSE_SUBMITTER);
                if (courseSubmitter != null) {
                    request.setAttribute("courseSubmitter", courseSubmitter);
                    request.getRequestDispatcher("/WEB-INF/jsp/mainPage/courseSubmitterMain.jsp").forward(request, response);
                    return;
                }
            }

            {
                PerformanceSubmitter performanceSubmitter = (PerformanceSubmitter) getRoleDetails(request, Authority.RoleIndex.PERFORMANCE_SUBMITTER);
                if (performanceSubmitter != null) {
                    request.setAttribute("performanceSubmitter", performanceSubmitter);
                    request.getRequestDispatcher("/WEB-INF/jsp/mainPage/performanceSubmitterMain.jsp").forward(request, response);
                    return;
                }
            }

            {
                WorkTeam workTeam = getRoleDetails(request, Authority.RoleIndex.WORK_TEAM);
                if (workTeam != null) {
                    request.setAttribute("workTeam", workTeam);
                    request.getRequestDispatcher("/WEB-INF/jsp/mainPage/workTeamMain.jsp").forward(request, response);
                    return;
                }
            }

            {
                CadreSubmitter cadreSubmitter = getRoleDetails(request, Authority.RoleIndex.CADRE_SUBMITTER);
                if (cadreSubmitter != null) {
                    request.setAttribute("cadreSubmitter", cadreSubmitter);
                    request.getRequestDispatcher("/WEB-INF/jsp/mainPage/cardeSubmitterMain.jsp").forward(request, response);
                    return;
                }
            }

            {
                AttendSubmitter attendSubmitter = getRoleDetails(request, Authority.RoleIndex.ATTEND_SUBMITTER);
                if (attendSubmitter != null) {
                    request.setAttribute("attendSubmitter", attendSubmitter);
                    request.getRequestDispatcher("/WEB-INF/jsp/mainPage/attendSubmitterMain.jsp").forward(request, response);
                    return;
                }
            }

            session.removeAttribute("isMultiRoles");
            session.removeAttribute("selectedRoleIndex");
            request.setAttribute("error", "您無權限使用此系統，請洽系統管理員");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "發生異常錯誤，請洽系統管理員");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }
    }


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            request.setAttribute("error", "您無權限使用此系統，請洽系統管理員");
            request.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(request, response);
            return;
        }

        List<Authority.RoleIndex> roles = account.getAuthority().getCurrentRoles();
        if (roles.size() == 1) {
            response.sendRedirect("main");
            return;
        }
        try {
            Integer roleValue = Integer.valueOf(request.getParameter("role"));
            Authority.RoleIndex roleIndex = Authority.RoleIndex.parse(roleValue);

            // test role index exists
            roleIndex.toString();

            session.setAttribute("selectedRoleIndex", roleIndex);
            session.setAttribute("isMultiRoles", "Y");
            account.setCurrentRole(roleIndex);
            session.setAttribute("account", account);

            response.sendRedirect("main");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("logout");
            return;
        }

    }

    private <T extends Role> T getRoleDetails(HttpServletRequest request, Authority.RoleIndex roleIndex) {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        if (account == null || roleIndex == null) {
            return null;
        }
        Authority authority = account.getAuthority();
        List<Authority.RoleIndex> roles = authority.getCurrentRoles();
        Authority.RoleIndex selectedIdx = null;
        final int size = roles.size();
        if (size > 1) {
            selectedIdx = (Authority.RoleIndex) session.getAttribute("selectedRoleIndex");
            if (!roles.contains(selectedIdx))
                return null;
        } else if (size == 1) {
            selectedIdx = roles.get(0);
        } else {
            return null;
        }

        if (roleIndex == selectedIdx) {
            return (T) account.getRoleDetail(selectedIdx);
        }

        return null;
    }
}
