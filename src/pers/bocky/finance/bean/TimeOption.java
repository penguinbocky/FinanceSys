package pers.bocky.finance.bean;

public enum TimeOption {
	LATEST_30("最近30天"), AVG_MONTH("月平均"), LAST_MONTH("上个自然月"), CURRENT_MONTH("当前自然月");
	
	private String timeOptionName;
	
	private TimeOption(String timeOptionName) {
		this.timeOptionName = timeOptionName;
	}

	public String getTimeOptionName() {
		return timeOptionName;
	}
}
