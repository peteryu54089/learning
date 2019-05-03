package dao.impl;

import model.Counsel;
import model.HasSchoolSchema;
import model.IndividualCounsel;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

class BaseCounselDaoImpl extends BaseDao {

    BaseCounselDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
    }

    void setCounselPreparedStatement(PreparedStatement preparedStatement, Counsel counsel) throws SQLException {
        int i = 0;
        preparedStatement.setTimestamp(++i, counsel.getStartTime());
        preparedStatement.setTimestamp(++i, counsel.getEndTime());
        preparedStatement.setString(++i, counsel.getTitle());
        if (counsel.getFileId() == null) {
            preparedStatement.setNull(++i, Types.INTEGER);
        } else {
            preparedStatement.setInt(++i, counsel.getFileId());
        }
        if (counsel.getLocation() == null) {
            preparedStatement.setNull(++i, Types.VARCHAR);
        } else {
            preparedStatement.setString(++i, counsel.getLocation());
        }
        if (counsel.getDescription() == null) {
            preparedStatement.setNull(++i, Types.VARCHAR);
        } else {
            preparedStatement.setString(++i, counsel.getDescription());
        }
        preparedStatement.setString(++i, counsel.getCounselor());
        preparedStatement.setString(++i, counsel.getSubmitter());
        if (counsel instanceof IndividualCounsel) {
            preparedStatement.setInt(++i, ((IndividualCounsel) counsel).getRgno());
        }
        if (counsel.getId() >= 0) {
            preparedStatement.setInt(++i, counsel.getId());
        }
    }
}
