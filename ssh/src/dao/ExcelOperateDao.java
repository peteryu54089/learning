package dao;


import model.Cadre;

import java.sql.SQLException;

public interface ExcelOperateDao {

    void insertToPresident(Cadre cadre) throws SQLException;
}
