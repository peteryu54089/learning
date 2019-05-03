package dao;

import java.sql.SQLException;

import model.MisSystemData;


public interface MisSystemDao {
	
	MisSystemData getStuAffairsSbjYS(int pSysNum) throws Exception;
}
