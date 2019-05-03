package util;

import model.Account;
import model.Authority.RoleIndex;
import model.role.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class AuthCheck {

    public final static int All = 0;
    public final static int Manager = 1;
    public final static int Teacher = 2;
    public final static int Student = 3;
    public final static int Counselor = 4;
    public final static int CourseSubmitter = 5;
    public final static int PerformanceSubmitter = 6;

    public static boolean isAuth(HttpServletRequest request) {
        boolean result = false;
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            System.out.println("Not authenticated.");
        } else {
            result = true;
        }
        return result;
    }

    public static boolean isAuth(HttpServletRequest request, RoleIndex roleIndex) {
        boolean result = false;
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            System.out.println("Not authenticated.");
        } else if (account.getAuthority().checkRole(roleIndex)) {
            result = true;
        }
        return result;
    }

    public static boolean isAuth(HttpServletRequest request, int position) {
        HttpSession session = request.getSession();
        Manager manager = (Manager) session.getAttribute("manager");
        Teacher teacher = (Teacher) session.getAttribute("teacher");
        Student student = (Student) session.getAttribute("student");
        Counselor counselor = (Counselor) session.getAttribute("counselor");
        CourseSubmitter courseSubmitter = (CourseSubmitter) session.getAttribute("courseSubmitter");
        PerformanceSubmitter performanceSubmitter = (PerformanceSubmitter) session
                .getAttribute("performanceSubmitter");
        if (manager == null && teacher == null && student == null && counselor == null
                && courseSubmitter == null && performanceSubmitter == null) {
            System.out.println("false1");
            return false;
        }
        if (position == All) {
            System.out.println("true2");
            return true;
        } else if (position == Manager) {
            if (manager == null) {
                System.out.println("false3");
                return false;
            }
        } else if (position == Teacher) {
            if (teacher == null) {
                System.out.println("false4");
                return false;
            }
        } else if (position == Student) {
            if (student == null) {
                System.out.println("false5");
                return false;
            }
        } else if (position == Counselor) {
            if (counselor == null) {
                System.out.println("false6");
                return false;
            }
        } else if (position == CourseSubmitter) {
            if (courseSubmitter == null) {
                System.out.println("false7");
                return false;
            }
        } else if (position == PerformanceSubmitter) {
            if (performanceSubmitter == null) {
                System.out.println("false8");
                return false;
            }
        }
        System.out.println("true6");
        return true;

    }
}
