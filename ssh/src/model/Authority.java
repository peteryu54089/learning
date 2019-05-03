package model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Authority {
    /**
     * 角色代碼
     */
    private int roleCode;

    public Authority(int roleCode) {
        checkRoleIndexValues(roleCode);
        this.roleCode = roleCode;
    }

    public void setRoleCode(int value) {
        checkRoleIndexValues(value);
        this.roleCode = value;
    }

    public int getRoleCode() {
        return roleCode;
    }

    /**
     * @return 是否為超級管理員
     */
    @Deprecated
    public boolean isAdmin() {
        return checkRole(RoleIndex.ADMIN);
    }

    /**
     * @return 是否為校管理者
     */
    public boolean isManager() {
        return checkRole(RoleIndex.MANAGER);
    }

    /**
     * @return 是否為工作小組
     */
    public boolean isWorkTeam() {
        return checkRole(RoleIndex.WORK_TEAM);
    }

    /**
     * @return 是否為學生
     */
    public boolean isStudent() {
        return checkRole(RoleIndex.STUDENT);
    }

    /**
     * @return 是否為導師
     */
    public boolean isTutor() {
        return checkRole(RoleIndex.TUTOR);
    }

    /**
     * @return 是否為授課老師
     */
    public boolean isTeacher() {
        return checkRole(RoleIndex.TEACHER);
    }

    /**
     * @return 是否為課程諮詢老師
     */
    public boolean isCounselor() {
        return checkRole(RoleIndex.COUNSELOR);
    }

    /**
     * @return 是否為課程學習成果提交人員
     */
    public boolean isCourseSubmitter() {
        return checkRole(RoleIndex.COURSE_SUBMITTER);
    }

    /**
     * @return 是否為多元表現提交人員
     */
    public boolean isPerformanceSubmitter() {
        return checkRole(RoleIndex.PERFORMANCE_SUBMITTER);
    }

    /**
     * @return 是否為幹部經歷紀錄建立人員
     */
    public boolean isCadreSubmitter() {
        return checkRole(RoleIndex.CADRE_SUBMITTER);
    }

    /**
     * @return 是否為出缺勤紀錄建立人員
     */
    public boolean isAttendSubmitter() {
        return checkRole(RoleIndex.ATTEND_SUBMITTER);
    }

    /**
     * 設為超級管理員
     *
     * @param value 是否給予權限
     */
    @Deprecated
    public void setAdmin(boolean value) {
        setRoleByBoolean(RoleIndex.ADMIN, value);
    }

    /**
     * 設為校管理者
     *
     * @param value 是否給予權限
     */
    public void setManager(boolean value) {
        setRoleByBoolean(RoleIndex.MANAGER, value);
    }

    /**
     * 設為工作小組
     *
     * @param value 是否給予權限
     */
    public void setWorkTeam(boolean value) {
        setRoleByBoolean(RoleIndex.WORK_TEAM, value);
    }

    /**
     * 設為學生
     *
     * @param value 是否給予權限
     */
    public void setStudent(boolean value) {
        setRoleByBoolean(RoleIndex.STUDENT, value);
    }

    /**
     * 設為導師
     *
     * @param value 是否給予權限
     */
    public void setTutor(boolean value) {
        setRoleByBoolean(RoleIndex.TUTOR, value);
    }

    /**
     * 設為授課老師
     *
     * @param value 是否給予權限
     */
    public void setTeacher(boolean value) {
        setRoleByBoolean(RoleIndex.TEACHER, value);
    }

    /**
     * 設為課程諮詢老師
     *
     * @param value 是否給予權限
     */
    public void setCounselor(boolean value) {
        setRoleByBoolean(RoleIndex.COUNSELOR, value);
    }

    /**
     * 設為課程學習成果提交人員
     *
     * @param value 是否給予權限
     */
    public void setCourseSubmitter(boolean value) {
        setRoleByBoolean(RoleIndex.COURSE_SUBMITTER, value);
    }

    /**
     * 設為多元表現提交人員
     *
     * @param value 是否給予權限
     */
    public void setPerformanceSubmitter(boolean value) {
        setRoleByBoolean(RoleIndex.PERFORMANCE_SUBMITTER, value);
    }

    /**
     * 設為幹部經歷紀錄建立人員
     *
     * @param value 是否給予權限
     */
    public void setCadreSubmitter(boolean value) {
        setRoleByBoolean(RoleIndex.CADRE_SUBMITTER, value);
    }

    /**
     * 設為出缺勤紀錄建立人員
     *
     * @param value 是否給予權限
     */
    public void setAttendSubmitter(boolean value) {
        setRoleByBoolean(RoleIndex.ATTEND_SUBMITTER, value);
    }

    public List<RoleIndex> getCurrentRoles() {
        return Arrays.stream(RoleIndex.values()).filter(this::checkRole).collect(Collectors.toList());
    }

    public String getCurrentRolesStr() {
        return Arrays.stream(RoleIndex.values())
                .filter(this::checkRole)
                .map(RoleIndex::toString)
                .collect(Collectors.joining(", "));
    }

    /**
     * @param roleCode
     */
    private void checkRoleIndexValues(int roleCode) {
        int copyRoleCode = roleCode;
        for (RoleIndex roleIndex : RoleIndex.values()) {
            //去除該role的代碼
            copyRoleCode &= ~(roleIndex.value);
        }

        // 檢查是否全為0
        if (copyRoleCode != 0) {
            throw new IllegalArgumentException("角色代碼超出範圍");
        }
    }

    public boolean checkRole(RoleIndex roleIndex) {
        return (this.roleCode & roleIndex.value) > 0;
    }

    public boolean containsRole(RoleIndex... roleIndexes) {
        for (final RoleIndex roleIndex : roleIndexes) {
            if ((this.roleCode & roleIndex.value) > 0)
                return true;
        }
        return false;
    }

    /**
     * 授予權限
     *
     * @param roleIndex role
     */
    public void grantRole(RoleIndex roleIndex) {
        this.roleCode |= roleIndex.value;
    }

    /**
     * 撤銷權限
     *
     * @param roleIndex role
     */
    public void revokeRole(RoleIndex roleIndex) {
        this.roleCode &= ~(roleIndex.value);
    }

    private void setRoleByBoolean(RoleIndex roleIndex, boolean value) {
        if (value) {
            grantRole(roleIndex);
        } else {
            revokeRole(roleIndex);
        }
    }

    public enum RoleIndex {
        /**
         * 無 None
         */
        NONE(0),

        /**
         * 管理員 Admin
         */
        ADMIN(1),

        /**
         * 校管理者 Manager
         */
        MANAGER(2),

        /**
         * 工作小組 Work Team
         */
        WORK_TEAM(4),

        /**
         * 學生 Student
         */
        STUDENT(8),

        /**
         * 導師 Tutor
         */
        TUTOR(16),

        /**
         * 授課老師 Teacher
         */
        TEACHER(32),

        /**
         * 課程諮詢老師 Counselor
         */
        COUNSELOR(64),

        /**
         * 課程學習成果提交人員 Course Submitter
         */
        COURSE_SUBMITTER(128),

        /**
         * 多元表現提交人員 Performance Submitter
         */
        PERFORMANCE_SUBMITTER(256),

        /**
         * 幹部經歷紀錄建立人員 Cadre Submitter
         */
        CADRE_SUBMITTER(512),

        /**
         * 出缺勤紀錄建立人員 Attend Submitter
         */
        ATTEND_SUBMITTER(1024);

        private final int value;

        RoleIndex(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }

        @Override
        public String toString() {
            if (this.value == MANAGER.value) return "校管理者";
            if (this.value == WORK_TEAM.value) return "工作小組";
            if (this.value == STUDENT.value) return "學生";
            if (this.value == TUTOR.value) return "導師";
            if (this.value == TEACHER.value) return "授課老師";
            if (this.value == COUNSELOR.value) return "課程諮詢老師";
            if (this.value == COURSE_SUBMITTER.value) return "課程學習成果提交人員";
            if (this.value == PERFORMANCE_SUBMITTER.value) return "多元表現提交人員";
            if (this.value == CADRE_SUBMITTER.value) return "幹部經歷紀錄建立人員";
            if (this.value == ATTEND_SUBMITTER.value) return "出缺勤紀錄建立人員";
            if (this.value == NONE.value) return "N/A";
            if (this.value == ADMIN.value) return "超級管理員";

            throw new IllegalArgumentException();
        }

        public static RoleIndex parse(Integer value) {
            if (value == null) return null;
            for (RoleIndex i : values()) {
                if (i.value == value) return i;
            }

            return null;
        }
    }
}
