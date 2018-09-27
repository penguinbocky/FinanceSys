package pers.bocky.finance.dao;

import pers.bocky.finance.util.DbUtility;
import pers.bocky.finance.util.LoggerUtil;

public class BaseDao {
	protected static DbUtility dbUtil = DbUtility.DBUTIL_MYSQL;
	protected static LoggerUtil logger = LoggerUtil.SQLogger;
	
	public static boolean dumpAllData(String path) {
		return dbUtil.dumpDB(null, path);
	}
	
}
