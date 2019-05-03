/**
 * 台北科技大學 計算機與網路中心 校務資訊組
 *
 * @author WSY
 * 檔案名稱：XXX.java
 * 說明：XXXX
 * 創建日期：2019年1月9日
 */
package util;

import dao.MisSystemDao;
import dao.SystemConfigDao;
import dao.impl.MisSystemDaoImpl;
import dao.impl.SystemConfigDaoImpl;
import model.MisSystemData;
import model.SystemConfig;

/**
 * @author WSY
 *
 */
public class MisSystemUtil {

    public static MisSystemData getStuAffairsSbjYS(final String schema) throws Exception {
//		SystemConfigDao systemConfigDao = new SystemConfigDaoImpl(()-> "LEARNING");
        SystemConfigDao systemConfigDao = new SystemConfigDaoImpl(() -> schema);
        int sys_num = (systemConfigDao.isNightSchool() ? 52 : 5);

        SystemConfig.SchoolInfo schoolInfo = systemConfigDao.getSystemConfig().getSchoolInfo();

        MisSystemDao misSystemDao = new MisSystemDaoImpl(() -> (SchemaUtil.convertSchoolIdToHSASchema(schoolInfo.getId())));
        MisSystemData misSystemData = misSystemDao.getStuAffairsSbjYS(sys_num);    // 學務系統學年期

        return misSystemData;
    }
}
