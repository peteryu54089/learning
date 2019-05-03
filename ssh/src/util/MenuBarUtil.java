/**
 * 台北科技大學 計算機與網路中心 校務資訊組
 * @author WSY
 * 檔案名稱：XXX.java
 * 說明：XXXX
 * 創建日期：2019年1月10日
 */
package util;

import model.Authority;

/**
 * @author WSY
 *
 */
public class MenuBarUtil {
	public static String getMenuBarBySelectedRoleIndex(Authority.RoleIndex pSelectedRoleIndex){
    	String result = "";

    	switch(pSelectedRoleIndex){
    		case ADMIN:
    			result = "adminBar.jsp";
    			break;
    		case MANAGER:
    			result = "managerBar.jsp";
    			break;
    		case WORK_TEAM:
    			result = "workTeamBar.jsp";
    			break;
    		case STUDENT:
    			result = "studentBar.jsp";
    			break;
    		case TUTOR:
    			result = "tutorBar.jsp";
    			break;
    		case TEACHER:
    			result = "teacherBar.jsp";
    			break;
    		case COUNSELOR:
    			result = "counselorBar.jsp";
    			break;
    		case COURSE_SUBMITTER:
    			result = "courseSubmitterBar.jsp";
    			break;
    		case PERFORMANCE_SUBMITTER:
    			result = "performanceSubmitterBar.jsp";
    			break;
    		case CADRE_SUBMITTER:
    			result = "cadreSubmitterBar.jsp";
    			break;
    		default:
    				break;
    	}

    	return result;
    }
}
