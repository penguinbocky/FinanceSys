package pers.bocky.finance.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import pers.bocky.finance.dao.BorrowDao;
import pers.bocky.finance.dao.CategoryDao;
import pers.bocky.finance.dao.ConsumeDao;
import pers.bocky.finance.dao.DepositDao;
import pers.bocky.finance.dao.LendDao;
import pers.bocky.finance.dao.TypeDao;
import pers.bocky.finance.util.DaoResponse;
import pers.bocky.finance.view.WillBeInMainTabbed;
import pers.bocky.finance.view.WillBeInTabbed;
import pers.bocky.finance.view.impl.BorrowPanel;
import pers.bocky.finance.view.impl.CategoryConfigPanel;
import pers.bocky.finance.view.impl.ConsumePanel;
import pers.bocky.finance.view.impl.DepositPanel;
import pers.bocky.finance.view.impl.LendPanel;
import pers.bocky.finance.view.impl.TypeConfigPanel;

public class ButtonActionListener implements ActionListener {

	private WillBeInTabbed panel;
	
	public ButtonActionListener(WillBeInTabbed panel) {
		super();
		this.panel = panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Detected action event > " + e.getActionCommand());
		String action = e.getActionCommand();
		if (action.contains("save")) {
			if (JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(null, "确定要添加该条记录？")) {
				return;
			} else {
				DaoResponse response;
				switch (e.getActionCommand()) {
				case "saveDepositRecord":
					DepositPanel dp = (DepositPanel) panel;
					response = DepositDao.saveNewDeposit(dp.formSaveBean());
					if (response == DaoResponse.SAVE_SUCCESS) {
						dp.loadDatagrid();
						dp.clearInput();
					}
					
					break;
					
				case "saveConsumeRecord":
					ConsumePanel cp = (ConsumePanel) panel;
					response = ConsumeDao.saveNewConsume(cp.formSaveBean());
					if (response == DaoResponse.SAVE_SUCCESS) {
						cp.loadDatagrid();
						cp.clearInput();
					}
					
					break;
					
				case "saveBorrowRecord":
					BorrowPanel bp = (BorrowPanel) panel;
					response = BorrowDao.saveNewBorrow(bp.formSaveBean());
					if (response == DaoResponse.SAVE_SUCCESS) {
						bp.loadDatagrid();
						bp.clearInput();
					}
					
					break;
					
				case "saveLendRecord":
					LendPanel lp = (LendPanel) panel;
					response = LendDao.saveNewLend(lp.formSaveBean());
					if (response == DaoResponse.SAVE_SUCCESS) {
						lp.loadDatagrid();
						lp.clearInput();
					}
					
					break;
				case "saveCategoryConfig":
					CategoryConfigPanel ccp = (CategoryConfigPanel) panel;
					response = CategoryDao.saveNewCategory(ccp.formSaveBean());
					if (response == DaoResponse.SAVE_SUCCESS) {
						ccp.loadDatagrid();
						ccp.clearInput();
					} else if (response == DaoResponse.EXISTED) {
						JOptionPane.showMessageDialog(null, "该名称已存在，请输入其他名称");
					}
					
					break;

				case "saveTypeConfig":
					TypeConfigPanel tcp = (TypeConfigPanel) panel;
					response = TypeDao.saveNewType(tcp.formSaveBean());
					if (response == DaoResponse.SAVE_SUCCESS) {
						tcp.loadDatagrid();
						tcp.clearInput();
					} else if (response == DaoResponse.EXISTED) {
						JOptionPane.showMessageDialog(null, "该名称已存在，请输入其他名称");
					}
					
					break;
					
				default:
					break;
				}
			}
		} else if (action.contains("refresh")) {
			switch (action) {
			case "refreshDataGrid":
				((WillBeInMainTabbed) panel).loadDatagrid();
				break;

			default:
				break;
			}
		}
		
	}

}
