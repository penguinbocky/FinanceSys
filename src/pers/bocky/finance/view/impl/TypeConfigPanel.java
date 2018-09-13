package pers.bocky.finance.view.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import pers.bocky.finance.bean.CategoryBean;
import pers.bocky.finance.bean.TypeBean;
import pers.bocky.finance.component.DataGrid;
import pers.bocky.finance.dao.CategoryDao;
import pers.bocky.finance.dao.TypeDao;
import pers.bocky.finance.listener.ButtonActionListener;
import pers.bocky.finance.listener.MyDocument;
import pers.bocky.finance.view.WillBeInConfigTabbed;

public class TypeConfigPanel extends JPanel implements WillBeInConfigTabbed{

	private static final long serialVersionUID = 1L;

	private final String PANEL_TYPE = "类型";
	private DataGrid datagrid;
	private ButtonActionListener listener;
	private JTextField typeField;
	private JTextField descField;
	private JComboBox<CategoryBean> categoryDropdown;
	
	public TypeConfigPanel() {
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
		JLabel filterLabel = new JLabel("请选择过滤条件：");
		
		panel.add(refresh);
		panel.add(filterLabel);
		
		return panel;
	}

	private JScrollPane createMiddlePanel() {
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(new Color(199, 237, 204, 255));
		
		final String[] COL_NAMES = {"whatever", "类别ID", "类别名称", "类型ID", "类型名称", "类型描述", "最后更新于", "创建时间"};
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
		save.setActionCommand("saveTypeConfig");
		save.addActionListener(listener);
		
		JLabel categoryLabel = new JLabel("类别");
		
		categoryDropdown = new JComboBox<CategoryBean>();
		List<CategoryBean> list = CategoryDao.fetchAllCategories();
		final CategoryBean[] actions = list.toArray(new CategoryBean[0]);
		categoryDropdown.setModel(new DefaultComboBoxModel<CategoryBean>(actions));
		
		JLabel typeLabel = new JLabel("类型名称");
		typeField = new JTextField();
		MyDocument mm = new MyDocument(20, save, typeField);
		typeField.setDocument(mm);//For length limit
		typeField.getDocument().addDocumentListener(mm);//For check input
		JLabel descLabel = new JLabel("类型描述");
		descField = new JTextField();
		
		inputPanel.add(categoryLabel);
		inputPanel.add(categoryDropdown);
		inputPanel.add(typeLabel);
		inputPanel.add(typeField);
		inputPanel.add(descLabel);
		inputPanel.add(descField);
		inputPanel.add(save);
		
		panel.add(inputPanel);
		
		return panel;
	}

	@Override
	public void loadDatagrid() {
		List<TypeBean> list = TypeDao.fetchAllTypesAndCategories();
		List<Vector<String>> dataVectorList = new ArrayList<Vector<String>>();
		for (int i = 0; i < list.size(); i++) {
			TypeBean bean = list.get(i);
			Vector<String> v = new Vector<String>();
			v.add(bean.getCategoryId().toString());
			v.add(bean.getCategoryId().toString());//Dummy one
			v.add(bean.getCategoryName());
			v.add(bean.getTypeId().toString());
			v.add(bean.getTypeName());
			v.add(bean.getDescription());
			v.add(bean.getLastUpdateTs().toString());
			v.add(bean.getAddTs().toString());
			dataVectorList.add(v);
		}
		datagrid.setData(dataVectorList);
	}

	public TypeBean formSaveBean() {
		TypeBean bean = new TypeBean();
		bean.setCategoryId(
				Integer.parseInt(
						((CategoryBean)categoryDropdown.getSelectedItem()).getCategoryId().toString()
				)
		);
		bean.setTypeName(typeField.getText().trim());
		bean.setDescription(descField.getText().trim());
		System.out.println("The formed save bean > " + bean);
		return bean;
	}
	
	public void clearInput() {
		typeField.setText("");
		descField.setText("");
	}
	
	@Override
	public String getTabTitle() {
		return PANEL_TYPE;
	}

}
