package pers.bocky.finance.bean;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class BaseBean {

	abstract BaseBean buildFrom(ResultSet rs) throws SQLException;
}
