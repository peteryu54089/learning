package dao;

import model.Profit;

import java.sql.SQLException;
import java.util.List;

public interface ProfitDao {
    List<Profit> getProfit(int year, int sem, String staffCode);
}
