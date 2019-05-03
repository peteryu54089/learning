package dao.impl;

import dao.RoleDao;
import model.Authority.RoleIndex;
import model.HasSchoolSchema;
import model.role.Role;

import java.sql.SQLException;

public class RoleDaoImpl extends BaseDao implements RoleDao {

    public RoleDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
    }

    @Override
    public Role getDetailByRegNumber(String regNumber, RoleIndex roleIndex) throws SQLException {
        RoleDao roleDao = null;
        Role role = null;
        switch (roleIndex) {
            case ADMIN:
                roleDao = new AdminDaoImpl(this);
                break;
            case MANAGER:
                roleDao = new ManagerDaoImpl(this);
                //role = roleDao.getDetailByRegNumber(regNumber, roleIndex);
                break;
            case WORK_TEAM:
                roleDao = new WorkTeamDaoImpl(this);
                break;
            case STUDENT:
                roleDao = new StudentDaoImpl(this);
                break;
            case TUTOR:
                roleDao = new TutorDaoImpl(this);
                break;
            case TEACHER:
                roleDao = new TeacherDaoImpl(this);
                break;
            case COUNSELOR:
                roleDao = new CounselorDaoImpl(this);
                break;
            case COURSE_SUBMITTER:
                roleDao = new CourseSubmitterDaoImpl(this);
                break;
            case PERFORMANCE_SUBMITTER:
                roleDao = new PerformanceSubmitterDaoImpl(this);
                break;
            case CADRE_SUBMITTER:
                roleDao = new CadreSubmitterDaoImpl(this);
                break;
            case ATTEND_SUBMITTER:
                roleDao = new AttendSubmitterDaoImpl(this);
                break;
            default:
                break;
        }
        if (roleDao != null) {
            role = roleDao.getDetailByRegNumber(regNumber, roleIndex);
        }
        return role;
    }
}
