package dao;

import model.CourseRecordSubmitRecord;
import model.PerformanceRecordSubmitRecord;
import model.RecordSubmitRecord;

import java.sql.SQLException;
import java.util.List;

public interface SubmitCourseDao {
    Report getReportByYearAndSem();

    Report getReportByYearAndSem(Integer rgno);

    List<Object> getDetails(Integer year, Integer sem, Integer q, Integer rgno) throws SQLException;

    class Report {
        private List<CourseRecordSubmitRecord> _ret1;//未驗證
        private List<CourseRecordSubmitRecord> _ret2;//認證成功未勾選
        private List<CourseRecordSubmitRecord> _ret3;//認證成功已勾選
        private List<CourseRecordSubmitRecord> _ret4;//認證成功已勾選已報送
        private List<CourseRecordSubmitRecord> _ret5;//認證失敗

        public Report(List<CourseRecordSubmitRecord> ret1,
                      List<CourseRecordSubmitRecord> ret2,
                      List<CourseRecordSubmitRecord> ret3,
                      List<CourseRecordSubmitRecord> ret4,
                      List<CourseRecordSubmitRecord> ret5) {
            _ret1 = ret1;
            _ret2 = ret2;
            _ret3 = ret3;
            _ret4 = ret4;
            _ret5 = ret5;
        }


        public List<CourseRecordSubmitRecord> get_ret1() {
            return _ret1;
        }

        public List<CourseRecordSubmitRecord> get_ret2() {
            return _ret2;
        }

        public List<CourseRecordSubmitRecord> get_ret3() {
            return _ret3;
        }

        public List<CourseRecordSubmitRecord> get_ret4() {
            return _ret4;
        }

        public List<CourseRecordSubmitRecord> get_ret5() {
            return _ret5;
        }
    }
}

