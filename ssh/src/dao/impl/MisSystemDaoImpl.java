package dao.impl;

import dao.MisSystemDao;
import dbconn.DbConn;
import model.HasSchoolSchema;
import model.MisSystemData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class MisSystemDaoImpl extends BaseDao implements MisSystemDao {

    public MisSystemDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
    }

    @Override
    public MisSystemData getStuAffairsSbjYS(int pSysNum) throws Exception {
    	MisSystemData misSystemData = null;
    	String sql = "SELECT * FROM MIS_SYSTEM WHERE SYS_NUM = ? ";
    	
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            preparedStatement.setInt(1, pSysNum);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
            	misSystemData = new MisSystemData();
            	misSystemData.getDataFromResultSet(resultSet);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
        }

		return misSystemData;
    }

}
