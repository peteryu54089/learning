package dao;

import model.PerformanceRecordSubmitRecord;

import java.util.List;

public interface SubmitPerformanceDao {
    Report getReportByYearAndSem();

    Report getReportByYearAndSem(Integer rgno);

    //TODO: ADD SUBMIT TIME
    List<Object> getDetails(Integer year, Integer rgno, Integer status, String date);

    class Report {
        private List<PerformanceRecordSubmitRecord> _uncheckedAndNotSubmit;
        private List<PerformanceRecordSubmitRecord> _checkedAndNotSubmit;
        private List<PerformanceRecordSubmitRecord> _checkedAndSubmitted;

        public Report(List<PerformanceRecordSubmitRecord> uncheckedAndNotSubmit,
                      List<PerformanceRecordSubmitRecord> checkedAndNotSubmit,
                      List<PerformanceRecordSubmitRecord> checkedAndSubmitted) {
            _uncheckedAndNotSubmit = uncheckedAndNotSubmit;
            _checkedAndNotSubmit = checkedAndNotSubmit;
            _checkedAndSubmitted = checkedAndSubmitted;
        }

        public List<PerformanceRecordSubmitRecord> getCheckedAndNotSubmit() {
            return _checkedAndNotSubmit;
        }

        public List<PerformanceRecordSubmitRecord> getCheckedAndSubmitted() {
            return _checkedAndSubmitted;
        }

        public List<PerformanceRecordSubmitRecord> getUncheckedAndNotSubmit() {
            return _uncheckedAndNotSubmit;
        }
    }
}
