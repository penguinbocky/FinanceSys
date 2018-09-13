package pers.bocky.finance.bean;

import java.util.Date;

public class LogBean {

	private String info;
	private Date date;
	
	public LogBean(String info, Date date) {
		super();
		this.info = info;
		this.date = date;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
