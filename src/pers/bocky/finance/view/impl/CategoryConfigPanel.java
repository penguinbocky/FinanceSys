package pers.bocky.finance.view.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import pers.bocky.finance.bean.CategoryBean;
import pers.bocky.finance.component.DataGrid;
import pers.bocky.finance.dao.CategoryDao;
import pers.bocky.finance.listener.ButtonActionListener;
import pers.bocky.finance.listener.MyDocument;
import pers.bocky.finance.view.WillBeInConfigTabbed;

public class CategoryConfigPanel extends JPanel implements WillBeInConfigTabbed{

	private static final long serialVersionUID = 1L;

	private final String PANEL_TYPE = "类别（大类）";
	private DataGrid datagrid;
	private ButtonActionListener listener;
	private JTextField categoryField;
	private JTextField descField;
	
	public CategoryConfigPanel() {
		super();
		listener = new ButtonActionListener(this);
		setStyles();
		createUI();
	}

	/**
	 * Styles configuration
	 */
	private void setStyles() {
		this.setLayout(new BorderLayout());
	}

	private void createUI() {
		add(createTopPanel(), BorderLayout.NORTH);
		add(createMiddlePanel(), BorderLayout.CENTER);
		add(createBottomPanel(), BorderLayout.SOUTH);
	}

	private Component createTopPanel() {
		JPanel panel = new JPanel();
		panel.setBackground(new Color(129, 195, 230));
		
		JButton refresh = new JButton("刷新");
		refresh.setActionCommand("refreshDataGrid");
		refresh.addActionListener(new ButtonActionListener(this));
		JLabel filterLabel = new JLabel("请选择过滤条件：");
		
		panel.add(refresh);
		panel.add(filterLabel);
		
		return panel;
	}

	private JScrollPane createMiddlePanel() {
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(new Color(199, 237, 204, 255));
		
		final String[] COL_NAMES = {"whatever", "whatever", "类别ID", "类别名称", "类别描述", "最后更新于", "创建时间"};
		datagrid = new DataGrid(COL_NAMES);
		scrollPane.setViewportView(datagrid);
		
		loadDatagrid();
		
		return scrollPane;
	}

	private JPanel createBottomPanel() {
		JPanel panel = new JPanel();
		JPanel inputPanel = new JPanel(new GridLayout(1, 0, 20, 0));
		panel.setBackground(new Color(204, 170, 126, 255));
		inputPanel.setOpaque(false);
		
		JButton save = new JButton("保存");
		save.setEnabled(false);
		save.setActionCommand("saveCategoryConfig");
		save.addActionListener(listener);
		
		JLabel categoryLabel = new JLabel("类别名称");
		categoryField = new JTextField();
		MyDocument mm = new MyDocument(20, save, categoryField);
		categoryField.setDocument(mm);//For length limit
		categoryField.getDocument().addDocumentListener(mm);//For check input
		JLabel descLabel = new JLabel("类别描述");
		descField = new JTextField();
		
		inputPanel.add(categoryLabel);
		inputPanel.add(categoryField);
		inputPanel.add(descLabel);
		inputPanel.add(descField);
		inputPanel.add(save);
		
		panel.add(inputPanel);
		
		return panel;
	}

	@Override
	public void loadDatagrid() {
		List<CategoryBean> list = CategoryDao.fetchAllCategories();
		List<Vector<String>> dataVectorList = new ArrayList<Vector<String>>();
		for (int i = 0; i < list.size(); i++) {
			CategoryBean bean = list.get(i);
			Vector<String> v = new Vector<String>();
			v.add(bean.getCategoryId().toString());//Dummy one
			v.add(bean.getCategoryId().toString());//Dummy one
			v.add(bean.getCategoryId().toString());
			v.add(bean.getCategoryName());
			v.add(bean.getDescription());
			v.add(bean.getLastUpdateTs().toString());
			v.add(bean.getAddTs().toString());
			dataVectorList.add(v);
		}
		datagrid.setData(dataVectorList);
	}

	public CategoryBean formSaveBean() {
		CategoryBean bean = new CategoryBean();
		bean.setCategoryName(categoryField.getText().trim());
		bean.setDescription(descField.getText().trim());
		System.out.println("The formed save bean > " + bean);
		return bean;
	}
	
	public void clearInput() {
		categoryField.setText("");
		descField.setText("");
	}
	
	@Override
	public String getTabTitle() {
		return PANEL_TYPE;
	}

}
