package pers.bocky.finance.view;

public interface WillBeInMainTabbed extends WillBeInTabbed {

	public String getTabTitle();

	public void createMainUI();
	
	public boolean hasMainUI();
	
	public void loadDatagrid();
	
}
