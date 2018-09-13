package pers.bocky.finance.view.impl;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.Iterator;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import pers.bocky.finance.dao.ReadMeDao;
import pers.bocky.finance.view.WillBeInMainTabbed;

public class ReadMePanel extends JPanel implements WillBeInMainTabbed {

	private static final long serialVersionUID = 1L;
	
	private boolean hasMainUI;
	
	@Override
	public String getTabTitle() {
		return "说明";
	}

	public ReadMePanel() {
		super();
	}


	private void createUI() {
		setLayout(new GridLayout(0, 1));
		
		loadDatagrid();
	}

	private Component getDetailPanel() {
		JScrollPane sp = new JScrollPane();
		JEditorPane ep = new JEditorPane("text/html",
				"<h1 style='text-align: center; background: #F8D080'>特别说明</h1>"
				+ "<div style='font-size: 16px; line-height: 16px; background: #2C3235; color: #1EAC13'>"
				+ "<p>本系统初次制作于<b>2018年6月9号</b><br/>"
				+ "<p>或有不尽完善之处，但基本功能可用<br/>"
				+ "<p>版本号：<b>1.0.0 (final release date: 2018/08/12)</b></p>"
				+ "<p>版本号：<b>2.0.0 (final release date: 2018/09/13)</b></p>"
				+ "<hr/>"
				+ "<p>Note:</p>"
				+ generateNotes()
				+ "</div>");
		ep.setToolTipText("如数据库有更新，请重启该程序。");
		ep.setEditable(false);
		ep.setOpaque(false);
		sp.setViewportView(ep);
		return sp;
	}

	private String generateNotes() {
		StringBuilder notes = new StringBuilder();;
		
		List<String> list = ReadMeDao.fetchAllNotes();
		
		for (Iterator<String> iterator = list.iterator(); iterator.hasNext();) {
			String string = iterator.next();
			notes.append("- " + string + "<br/>");
		}
		
		return notes.toString();
	}

	@Override
	public void loadDatagrid() {
		removeAll();
		add(getDetailPanel());
	}

	@Override
	public void createMainUI() {
		createUI();
		this.hasMainUI = true;
	}

	@Override
	public boolean hasMainUI() {
		return this.hasMainUI;
	}

}
