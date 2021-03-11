package pers.bocky.finance.view.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import pers.bocky.finance.dao.BaseDao;
import pers.bocky.finance.util.PropertiesUtil;
import pers.bocky.finance.view.WillBeInMainTabbed;
import pers.bocky.finance.view.impl.ConfigFrame.ConfigFrameLazyHolder;

public class MorePanel extends JPanel implements WillBeInMainTabbed {

	private static final long serialVersionUID = 1L;

	private JButton openConfigBtn;
	private JButton dumpDataBaseBtn;
	
	private final String DUMP_PATH = PropertiesUtil.getValueAsString("mysql.dump.path");
	
	public MorePanel() {
		setStyles();
		createMainUI();
	}

	private void setStyles() {
		setBackground(Color.DARK_GRAY);
		setLayout(new BorderLayout(50, 150));
	}
	
	@Override
	public String getTabTitle() {
		return "更多";
	}

	@Override
	public void createMainUI() {
		openConfigBtn = new JButton("打开配置窗口");
		openConfigBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		openConfigBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ConfigFrameLazyHolder.cf.start();
			}
		});
		
		dumpDataBaseBtn = new JButton("一键备份数据库所有数据");
		dumpDataBaseBtn.setToolTipText("文件生成目录：" + DUMP_PATH);
		dumpDataBaseBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(MorePanel.this, BaseDao.dumpAllData(DUMP_PATH), "结果", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
//		JPanel panelC = new JPanel(new GridLayout(0, 1, 2, 5));
//		panelC.setBackground(Color.LIGHT_GRAY);
		JPanel panelS = new JPanel(new GridLayout(0, 1, 2, 5));
		panelS.add(dumpDataBaseBtn);
//		JPanel panelE = new JPanel(new GridLayout(0, 1, 2, 5));
//		panelE.add();
//		JPanel panelW = new JPanel(new GridLayout(0, 1, 2, 5));
//		panelW.add();
		
//		add(panelC, BorderLayout.CENTER);
		add(openConfigBtn, BorderLayout.NORTH);
		add(panelS, BorderLayout.SOUTH);
//		add(panelW, BorderLayout.WEST);
//		add(panelE, BorderLayout.EAST);
	}

	@Override
	public boolean hasMainUI() {
		return true;
	}

	@Override
	public void loadDatagrid() {
		// TODO Auto-generated method stub

	}

}
