package pers.bocky.finance.view.impl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import pers.bocky.finance.bean.BorrowBean;
import pers.bocky.finance.bean.HistoryBean;
import pers.bocky.finance.bean.HistoryType;
import pers.bocky.finance.bean.LendBean;
import pers.bocky.finance.component.DataGrid;
import pers.bocky.finance.dao.BaseDao;
import pers.bocky.finance.util.DateUtil;

public class HistoryFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private final int BASE_WIDTH = 800;
	private final int BASE_HEIGHT = 500;
	private final Dimension d;
	private final int WIDTH;
	private final int HEIGHT;
	
	{
		d = Toolkit.getDefaultToolkit().getScreenSize();
		WIDTH = (int) (BASE_WIDTH * d.getWidth() / 1366);
		HEIGHT = (int) (BASE_HEIGHT * d.getHeight() / 768);
	}
	
	public HistoryFrame(int categoryId, int id, HistoryType historyType) throws HeadlessException {
		super();
		
		String title = null;
		switch (categoryId) {
		case BorrowBean.CATEGORY_ID: ;
		case LendBean.CATEGORY_ID: if (historyType == HistoryType.UPDATE_AMOUNT) title = "更新历史"; else title = "欠款还款历史"; break;
		default: title = "历史记录";
		}
		setTitle(title);
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
		add(getHistoryPanel(categoryId, id, historyType));
		start();
	}

	public void start() {
		setVisible(true);;
	}

	protected JScrollPane getHistoryPanel(int categoryId, int id, HistoryType historyType) {
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(new Color(199, 237, 204, 255));
		
		final String[] COL_NAMES = {"ID", "数量", "备注", "发生时间", "更新时间", "创建时间"};
		DataGrid historyDatagrid = new DataGrid(COL_NAMES, new String[] {"ID"}, null, null);
		
		HistoryBean paramBean = new HistoryBean();
		paramBean.setId(id);
		paramBean.setCategoryId(categoryId);
		paramBean.setHistoryType(historyType);
		List<HistoryBean> list = BaseDao.fetchHistoryRecs(paramBean);
		List<Vector<String>> dataVectorList = new ArrayList<Vector<String>>();
		for (int i = 0; i < list.size(); i++) {
			HistoryBean bean = list.get(i);
			Vector<String> v = new Vector<String>();
			v.add(bean.getHistoryId().toString());
			v.add(NumberFormat.getNumberInstance().format(bean.getAmount()));
			v.add(bean.getDescription());
			v.add(DateUtil.date2Str(bean.getOccurTs()));
			//below is the ts update operation occurs
			v.add(DateUtil.timestamp2Str(bean.getLastUpdateTs()));
			//below is the ts when last record was created
			v.add(DateUtil.timestamp2Str(bean.getAddTs()));
			dataVectorList.add(v);
		}
		historyDatagrid.setData(dataVectorList);
		
		scrollPane.setViewportView(historyDatagrid);
		return scrollPane;
	}
}
