package dao.impl;

import dao.FinalDao;
import dbconn.DbConn;
import model.Final;
import model.HasSchoolSchema;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FinalDaoImpl extends BaseDao implements FinalDao {
    public FinalDaoImpl(HasSchoolSchema hasSchoolSchemaObject) {
        super(hasSchoolSchemaObject);
    }


    @Override
    public int getPerformanceCount(String id_no) throws SQLException {
        String sql = "SELECT COUNT(*) FROM  NCNU_UPLOAD_DATA WHERE id_no = ? AND type != 'course' ";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            preparedStatement.setString(1, id_no);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("count(*)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }
        return 0;
    }

    @Override
    public int getCourseCount(String id_no) throws SQLException {
        String sql = "SELECT COUNT(*) FROM  NCNU_UPLOAD_DATA WHERE id_no = ? AND type = 'course' ";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            preparedStatement.setString(1, id_no);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("count(*)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }
        return 0;
    }

    @Override
    public boolean getByIdAndType(int id, String type, String idno) throws SQLException {
        String sql = "SELECT * FROM  NCNU_UPLOAD_DATA WHERE id_no = ? AND type = ? AND tid = ?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            int i = 1;
            preparedStatement.setString(i++, idno);
            preparedStatement.setString(i++, type);
            preparedStatement.setInt(i++, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }
        return true;
    }

    @Override
    public void insertToFinal(int id, String type, String idno) throws SQLException {
        String sql = "INSERT INTO  NCNU_UPLOAD_DATA (id_no, type, tid) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = null;
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            int i = 1;
            preparedStatement.setString(i++, idno);
            preparedStatement.setString(i++, type);
            preparedStatement.setInt(i++, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
        }
    }

    @Override
    public List<Final> getFinalByIdno(String idno) throws SQLException {
        String sql = "SELECT * FROM  NCNU_UPLOAD_DATA WHERE id_no = ?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Final> finalList = new ArrayList<>();
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            int i = 1;
            preparedStatement.setString(i++, idno);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Final _final = new Final(
                        resultSet.getInt("id"),
                        resultSet.getString("id_no"),
                        resultSet.getString("type"),
                        resultSet.getInt("tid")
                );
                finalList.add(_final);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }
        return finalList;
    }

    @Override
    public void deleteFinalByidAndIdno(int tid, String type, String idno) throws SQLException {
        String sql = "DELETE FROM  NCNU_UPLOAD_DATA WHERE  tid =  ? AND id_no = ? AND type = ?";
        PreparedStatement preparedStatement = null;
        try (Connection pcon = DbConn.getConnection(this.schoolSchema)) {
            preparedStatement = pcon.prepareStatement(sql);
            int i = 1;
            preparedStatement.setInt(i++, tid);
            preparedStatement.setString(i++, idno);
            preparedStatement.setString(i++, type);
            preparedStatement.execute();
            System.out.println(preparedStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
        }
    }

    @Override
    public List<Final> getAllByType(String type) throws SQLException {
        String sql = "SELECT * FROM  NCNU_UPLOAD_DATA WHERE type = ?";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Final> finalList = new ArrayList<>();
        try (Connection pcon = DbConn.getConnection(this)) {
            preparedStatement = pcon.prepareStatement(sql);
            int i = 1;
            preparedStatement.setString(i++, type);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Final _final = new Final(
                        resultSet.getInt("id"),
                        resultSet.getString("id_no"),
                        resultSet.getString("type"),
                        resultSet.getInt("tid")
                );
                finalList.add(_final);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null)
                preparedStatement.close();
            if (resultSet != null)
                resultSet.close();
        }
        return finalList;
    }
}
