package pers.bocky.finance.view.impl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import pers.bocky.finance.bean.LendHistoryBean;
import pers.bocky.finance.component.DataGrid;
import pers.bocky.finance.dao.LendDao;
import pers.bocky.finance.util.DateUtil;
import pers.bocky.finance.util.StringUtil;

public class LendHistoryFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private final int BASE_WIDTH = 460;
	private final int BASE_HEIGHT = 500;
	private final Dimension d;
	private final int WIDTH;
	private final int HEIGHT;
	
	{
		d = Toolkit.getDefaultToolkit().getScreenSize();
		WIDTH = (int) (BASE_WIDTH * d.getWidth() / 1366);
		HEIGHT = (int) (BASE_HEIGHT * d.getHeight() / 768);
	}
	
	public LendHistoryFrame(int lendId) throws HeadlessException {
		super();
		
		setTitle("欠款还款历史");
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((int)(d.getWidth() - WIDTH) / 2, (int)(d.getHeight() - HEIGHT) / 2, 
				WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);	
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		setResizable(true);
		getContentPane().setBackground(Color.GRAY);
		add(getHistoryPanel(lendId));
		start();
	}

	public void start() {
		setVisible(true);;
	}

	protected JScrollPane getHistoryPanel(int lendId) {
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(new Color(199, 237, 204, 255));
		
		final String[] COL_NAMES = {"ID", "ID", "数量", "备注", "发生时间", "最后更新于", "创建时间"};
		DataGrid historyDatagrid = new DataGrid(COL_NAMES);
		
		LendHistoryBean paramBean = new LendHistoryBean();
		paramBean.setLendId(lendId);
		List<LendHistoryBean> list = LendDao.fetchAllLendHistoryRecs(paramBean);
		List<Vector<String>> dataVectorList = new ArrayList<Vector<String>>();
		for (int i = 0; i < list.size(); i++) {
			LendHistoryBean bean = list.get(i);
			Vector<String> v = new Vector<String>();
			v.add(bean.getLendHistoryId().toString());
			v.add(bean.getLendHistoryId().toString());//Dummy one
			v.add(StringUtil.subZeroAndDot(bean.getAmount()));
			v.add(bean.getDescription());
			v.add(bean.getOccurTs() != null ? DateUtil.timestamp2DateStr(bean.getOccurTs()) : null);
			v.add(bean.getLastUpdateTs().toString());
			v.add(bean.getAddTs().toString());
			dataVectorList.add(v);
		}
		historyDatagrid.setData(dataVectorList);
		
		scrollPane.setViewportView(historyDatagrid);
		return scrollPane;
	}
}
