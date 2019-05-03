package servlet.management;

import dao.AutobiographyDao;
import dao.StudyPlanDao;
import dao.impl.AutobiographyDaoImpl;
import dao.impl.StudyPlanDaoImpl;
import dao.impl.SystemConfigDaoImpl;
import dao.impl.UploadFileDaoImpl;
import model.*;
import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/recordExporter")
@MultipartConfig
public class RecordExporterServlet extends BaseManageServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        Account account;
        if ((account = checkPermission(request, response)) == null)
            return;

        SystemConfigDaoImpl systemConfigDao = new SystemConfigDaoImpl(account);

        SystemConfig config = systemConfigDao.getSystemConfig();
        boolean isNight = systemConfigDao.isNightSchool();
        request.setAttribute("performanceConfig", config.getPerformance());
        request.setAttribute("isNight", isNight);

        request.getRequestDispatcher("/WEB-INF/jsp/managerPage/recordExporter.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        Account account;
        if ((account = checkPermission(request, response)) == null)
            return;
        Integer year;
        Integer sem;
        List<Integer> grades = new ArrayList<>();
        try {
            try {
                MisSystemData data = MisSystemUtil.getStuAffairsSbjYS(account.getSchoolSchema());
                year = data.getSbj_year();
                sem = data.getSbj_sem();
            } catch (Exception ee) {
                // TODO: 抓目前學期
                ee.printStackTrace();
                year = 105;
                sem = 1;
            }

            switch (request.getParameter("grades")) {
                case "12":
                    grades.add(1);
                    grades.add(2);
                    break;
                case "3":
                    grades.add(3);
                    break;
                default:
                    grades.add(3);
                    break;
            }
        } catch (NumberFormatException e) {
            response.setStatus(400);
            return;
        }

        Upload.getSaveRootDir(getServletContext());

        try {
            AutobiographyDao autobiographyDao = new AutobiographyDaoImpl(account);
            StudyPlanDao studyPlanDao = new StudyPlanDaoImpl(account);
            List<Autobiography> autobiographies = autobiographyDao.getByStuInfo(year, sem, grades);
            List<StudyPlan> studyPlans = studyPlanDao.getByStuInfo(year, sem, grades);

            File tmpFile = CacheFileUtils.createCacheFile(getServletContext(), ".zip");
            try (ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(tmpFile)) {
                zipArchiveOutputStream.setEncoding(StandardCharsets.UTF_8.displayName());
                zipArchiveOutputStream.setUseZip64(Zip64Mode.Always);
                Zip.addFile(zipArchiveOutputStream, new File(""), new File(String.valueOf(new Date().getTime())));

                List<ExportableFile> files = new LinkedList<>();
                files.addAll(autobiographies.stream().map(ExportableFile::new).collect(Collectors.toList()));
                files.addAll(studyPlans.stream().map(ExportableFile::new).collect(Collectors.toList()));

                for (ExportableFile file : files) {
                    Zip.addFile(zipArchiveOutputStream, file.src1, file.dst1);
                    Zip.addFile(zipArchiveOutputStream, file.src2, file.dst2);
                }

                zipArchiveOutputStream.closeArchiveEntry();
                zipArchiveOutputStream.flush();
                zipArchiveOutputStream.finish();
            }

            CacheFileUtils.sendRedirect(response, tmpFile);
        } catch (IOException e) {
            e.printStackTrace();
            response.setStatus(500);
        }
    }

    private class ExportableFile {
        private File src1 = null;
        private File src2 = null;
        private File dst1 = null;
        private File dst2 = null;

        private ExportableFile(Autobiography bio) {
            try {
                src1 = new File(UploadFileDaoImpl.getWebInfoPath(), bio.getMain_uploadFile().getFilePath());
                src1 = new File(UploadFileDaoImpl.getWebInfoPath(), bio.getSub_uploadFile().getFilePath());
                dst1 = Paths.get("自傳", String.valueOf(bio.getRgno()), bio.getId().toString(), "MAIN_" + bio.getMain_uploadFile().getFileName()).toFile();
                dst2 = Paths.get("自傳", String.valueOf(bio.getRgno()), bio.getId().toString(), "SUB_" + bio.getSub_uploadFile().getFileName()).toFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private ExportableFile(StudyPlan plan) {
            try {
                src1 = new File(UploadFileDaoImpl.getWebInfoPath(), plan.getMain_uploadFile().getFilePath());
                src1 = new File(UploadFileDaoImpl.getWebInfoPath(), plan.getSub_uploadFile().getFilePath());
                dst1 = Paths.get("學習計畫", String.valueOf(plan.getRgno()), plan.getId().toString(), "MAIN_" + plan.getMain_uploadFile().getFileName()).toFile();
                dst2 = Paths.get("學習計畫", String.valueOf(plan.getRgno()), plan.getId().toString(), "SUB_" + plan.getSub_uploadFile().getFileName()).toFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
