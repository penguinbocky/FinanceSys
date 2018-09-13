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

import pers.bocky.finance.bean.TypeBean;
import pers.bocky.finance.dao.BorrowDao;
import pers.bocky.finance.dao.ConsumeDao;
import pers.bocky.finance.dao.DepositDao;
import pers.bocky.finance.view.WillBeInMainTabbed;
import pers.bocky.finance.view.impl.ConfigFrame.ConfigFrameLazyHolder;

public class MorePanel extends JPanel implements WillBeInMainTabbed {

	private static final long serialVersionUID = 1L;

	private JButton openConfigBtn;
	private JButton calNetDepositBtn1;
	private JButton calAllNormalConsumeLatest30DaysBtn;
	private JButton calAvgNormalConsumeOfMonthBtn;
	private JButton calRemainingBorrowAmountFromRelationshipBtn;
	private JButton calAllNormalConsumeLastMonthBtn;
	private JButton calAllNormalConsumeFromThisMonthBtn;
	
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
		
		calNetDepositBtn1 = new JButton("根据（存款栏总和-支出栏的偿还首付/房贷总和）计算实际存款");
		calNetDepositBtn1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(MorePanel.this, calNetDeposit(), "计算结果", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		calAllNormalConsumeLatest30DaysBtn = new JButton("计算最近30天的日常消费支出");
		calAllNormalConsumeLatest30DaysBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(MorePanel.this, calAllNormalConsumeLatest30Days(), "计算结果", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		calAvgNormalConsumeOfMonthBtn = new JButton("计算每月日常消费支出平均值(不含当前月)");
		calAvgNormalConsumeOfMonthBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(MorePanel.this, calAvgNormalConsumeOfMonth(), "计算结果", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		calRemainingBorrowAmountFromRelationshipBtn = new JButton("计算剩余欠款(不含银行贷款)");
		calRemainingBorrowAmountFromRelationshipBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(MorePanel.this, calRemainingBorrowAmountFromRelationship(), "计算结果", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		calAllNormalConsumeLastMonthBtn = new JButton("计算上个自然月日常消费支出");
		calAllNormalConsumeLastMonthBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(MorePanel.this, calAllNormalConsumeLastMonth(), "计算结果", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		calAllNormalConsumeFromThisMonthBtn = new JButton("计算本月日常消费支出");
		calAllNormalConsumeFromThisMonthBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(MorePanel.this, calAllNormalConsumeFromThisMonth(), "计算结果", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		JPanel panelC = new JPanel(new GridLayout(0, 1, 2, 5));
		panelC.setBackground(Color.LIGHT_GRAY);
		panelC.add(calAllNormalConsumeLatest30DaysBtn);
		panelC.add(calAllNormalConsumeLastMonthBtn);
		panelC.add(calAllNormalConsumeFromThisMonthBtn);
		panelC.add(calAvgNormalConsumeOfMonthBtn);
		JPanel panelS = new JPanel(new GridLayout(0, 1, 2, 5));
		panelS.add(calNetDepositBtn1);
		JPanel panelE = new JPanel(new GridLayout(0, 1, 2, 5));
		panelE.add(calRemainingBorrowAmountFromRelationshipBtn);
		add(panelC, BorderLayout.CENTER);
		add(openConfigBtn, BorderLayout.NORTH);
		add(panelS, BorderLayout.SOUTH);
		add(new JPanel(), BorderLayout.WEST);
		add(panelE, BorderLayout.EAST);
	}

	protected Object calAllNormalConsumeFromThisMonth() {
		return ConsumeDao.calculateAmountFromThisMonthOfType(TypeBean.TypeIdMappedInDB.NORMAL_CONSUME.getId());
	}

	protected Object calAllNormalConsumeLastMonth() {
		return ConsumeDao.calculateAmountOfLastMonthhOfType(TypeBean.TypeIdMappedInDB.NORMAL_CONSUME.getId());
	}

	protected Object calRemainingBorrowAmountFromRelationship() {
		return BorrowDao.calAllBorrowAmountOfType(TypeBean.TypeIdMappedInDB.PAY_BACK_TO_RELATIONSHIPS.getId()) - BorrowDao.calAllBorrowHistoryAmountOfType(TypeBean.TypeIdMappedInDB.PAY_BACK_TO_RELATIONSHIPS.getId());
	}

	protected Object calAvgNormalConsumeOfMonth() {
		return ConsumeDao.calculateAvgMonthAmountOfType(TypeBean.TypeIdMappedInDB.NORMAL_CONSUME.getId());
	}

	protected Object calAllNormalConsumeLatest30Days() {
		return ConsumeDao.calculateAmountOfLatestMonthOfType(TypeBean.TypeIdMappedInDB.NORMAL_CONSUME.getId());
	}

	protected double calNetDeposit() {
		return ((double) Math.round(
				(DepositDao.calculateAllDepositRecsAmount() 
				- ConsumeDao.calculateAmountOfType(
						TypeBean.TypeIdMappedInDB.CONSUME_FOR_PAY_LOAN.getId(), 
						TypeBean.TypeIdMappedInDB.CONSUME_FOR_PAY_RELATIONSHIPS.getId()
						)
				) * 100)) / 100;
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
