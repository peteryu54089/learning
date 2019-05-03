package dao;

import model.SystemConfig;
import model.SystemConfig.SchoolInfo;

public interface SystemConfigDao {
    SystemConfig getSystemConfig();

    void saveSystemConfig(SystemConfig systemConfig);

    boolean isNightSchool();
}
