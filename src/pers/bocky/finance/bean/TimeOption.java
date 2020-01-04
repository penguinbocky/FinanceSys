package pers.bocky.finance.bean;

public enum TimeOption {
	YESTERDAY("昨天"), TODAY("今天"), AVG_DAY("日均"),
	LAST_WEEK("上周"), CURRENT_WEEK("当前周"), AVG_WEEK("周平均"),
	LATEST_30("最近30天"), AVG_MONTH("月平均"), 
	LAST_MONTH("上个自然月"), CURRENT_MONTH("当前自然月"),
	CUSTOMIZED("自定义周期");
	
	private String timeOptionName;
	
	private TimeOption(String timeOptionName) {
		this.timeOptionName = timeOptionName;
	}

	public String getTimeOptionName() {
		return timeOptionName;
	}
}
