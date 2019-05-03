package model;

import dao.RoleDao;
import dao.impl.RoleDaoImpl;
import model.Authority.RoleIndex;
import model.role.Role;
import util.DateUtils;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Account implements Authenticate, HasSchoolSchema {
    private int seqNo = 0;
    private String schoolSchema;
    private boolean exist;
    private String regNumber;
    private int termYear;
    private int termSemester;
    private String password;
    private String classCode;
    private String name;
    private String englishName;
    private String idNumber;
    private String email;
    private String mobileTelNo;
    private int roleCode;
    private Authority authority;
    private Map<RoleIndex, Role> roleMap;
    private Date birthDay;
    private String source;
    private String avatar;
    private Authority currentRoleAuthority;

    public Account(
            HasSchoolSchema hasSchoolSchema,
            String reg_number, int term_year, int term_semester,
            String password, String name, String english_name, String id_number, String email,
            String mobile_telno, Date birthDay, String source, int role_code, String avatar) throws SQLException {
        if (reg_number == null) {
            throw new IllegalArgumentException("帳號為null");
        } else if (hasSchoolSchema.getSchoolSchema() == null) {
            throw new IllegalArgumentException("系統設定錯誤 (schema == null)");
        }
        this.schoolSchema = hasSchoolSchema.getSchoolSchema();
        this.regNumber = reg_number;
        this.termYear = term_year;
        this.termSemester = term_semester;
        this.password = password;
        this.name = name;
        this.englishName = english_name;
        this.idNumber = id_number;
        this.email = email;
        this.mobileTelNo = mobile_telno;
        this.birthDay = birthDay;
        this.source = source;
        this.roleCode = role_code;
        this.avatar = avatar;
        this.authority = new Authority(role_code);
        this.currentRoleAuthority = null;
        updateRoleDetail();//TODO: 撈出所有子資料
        this.exist = true;
    }

    public void updateRoleDetail() {
        final List<RoleIndex> roles = this.authority.getCurrentRoles();
        // System.out.println("Authority List: " + (roles.stream().map(RoleIndex::toString).collect(Collectors.joining(", "))));
        roleMap = new HashMap<>();
        RoleDao roleDao = new RoleDaoImpl(this);
        for (RoleIndex roleIndex : roles) {
            try {
                roleMap.put(roleIndex, roleDao.getDetailByRegNumber(this.regNumber, roleIndex));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int getTermYear() {
        return termYear;
    }

    public void setTermYear(int termYear) {
        this.termYear = termYear;
    }

    public int getTermSemester() {
        return termSemester;
    }

    public void setTermSemester(int termSemester) {
        this.termSemester = termSemester;
    }

    @Deprecated
    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileTelNo() {
        return mobileTelNo;
    }

    public void setMobileTelNo(String mobileTelNo) {
        this.mobileTelNo = mobileTelNo;
    }

    public boolean isExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }

    public int getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(int roleCode) {
        this.roleCode = roleCode;
        this.authority = new Authority(roleCode);
    }

    public void grantRole(RoleIndex roleIndex) {
        this.setRoleCode(this.roleCode | roleIndex.value());
    }

    public void revokeRole(RoleIndex roleIndex) {
        this.setRoleCode(this.roleCode & (~roleIndex.value()));
    }

    public Authority getAuthority() {
        if (currentRoleAuthority != null)
            return currentRoleAuthority;
        return authority;
    }

    public <T extends Role> T getRoleDetail(RoleIndex roleIndex) {
        if (roleIndex == null) return null;
        final Role r = roleMap.get(roleIndex);
        if (r != null) {
            try {
                return (T) r;
            } catch (ClassCastException ignored) {

            }
        }
        return null;
    }

    @Override
    public boolean authCheck(HttpSession session) throws ClassCastException {
        boolean result = true;
        Object object = session.getAttribute("User");
        if (object == null || object instanceof User) {
            result = false;
        } else {
            User user = (User) object;
            if (!this.regNumber.equals(user.getId()) || !this.password.equals(user.getPassword())) {
                result = false;
            }
        }
        return result;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public String getBirthDayInMingoFormat() {
        return getBirthDayInMingoFormat("/");
    }

    public String getBirthDayInMingoFormat(String delimiters) {
        if (birthDay == null)
            return "";

        return DateUtils.formatDateAsMingo(birthDay, "yyy" + delimiters + "MM" + delimiters + "dd");
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(int seqNo) {
        this.seqNo = seqNo;
    }

    public String getSchoolSchema() {
        return schoolSchema;
    }

    public void setSchoolSchema(String schoolSchema) {
        this.schoolSchema = schoolSchema;
    }

    public void setCurrentRole(RoleIndex roleIndex) {
        this.currentRoleAuthority = new Authority(roleIndex.value());
    }

    public void clearCurrentRole() {
        this.currentRoleAuthority = null;
    }

//  public void setRoleDetail(RoleIndex roleIndex, Role role) {
//    this.roleList[roleIndex.ordinal()] = role;
//  }
}
