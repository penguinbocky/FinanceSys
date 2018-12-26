package pers.bocky.finance.view.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
import pers.bocky.finance.util.DaoResponse;
import pers.bocky.finance.view.WillBeInConfigTabbed;

public class TypeConfigPanel extends JPanel implements WillBeInConfigTabbed{

	private static final long serialVersionUID = 1L;

	private final String PANEL_TYPE = "类型";
	private DataGrid datagrid;
	private ButtonActionListener listener;
	private JTextField typeField;
	private JTextField descField;
	private JComboBox<CategoryBean> categoryDropdown;
	private JButton updateBtn;
	private JButton deleteBtn;
	
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
		datagrid.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				checkForButtons();
				int selectedRowIndex = datagrid.getSelectedRow();
				if (selectedRowIndex != -1) {
					if (e.getButton() == MouseEvent.BUTTON1) {
						fillFields(datagrid, selectedRowIndex);
					}
				}
			}
			
		});
		scrollPane.setViewportView(datagrid);
		
		loadDatagrid();
		
		return scrollPane;
	}

	protected void fillFields(DataGrid datagrid2, int selectedRowIndex) {
		if (datagrid != null && selectedRowIndex > -1) {
			String categoryId = (String) datagrid.getValueAt(selectedRowIndex, 1);
			String typeName = (String) datagrid.getValueAt(selectedRowIndex, 4);
			String desc = (String) datagrid.getValueAt(selectedRowIndex, 5);
			
			categoryDropdown.setSelectedIndex(getDropdownIndexByTypeId(Integer.parseInt(categoryId)));
			typeField.setText(typeName);
			descField.setText(desc);
		}
	}

	private int getDropdownIndexByTypeId(int categoryId) {
		int size = categoryDropdown.getItemCount();
		for (int i = 0; i < size; i++) {
			CategoryBean bean = categoryDropdown.getItemAt(i);
			if (bean.getCategoryId() == categoryId) {
				return i;
			}
		}
		return -1;
	}

	protected void checkForButtons() {
		if (datagrid.getSelectedRow() == -1) {
			updateBtn.setEnabled(false);
			deleteBtn.setEnabled(false);
		} else {
			updateBtn.setEnabled(true);
			deleteBtn.setEnabled(true);
		}
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
		
		updateBtn = new JButton("更新");
		updateBtn.setEnabled(false);
		updateBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				updateRecord();
			}
		});
		
		deleteBtn = new JButton("删除");
		deleteBtn.setEnabled(false);
		deleteBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteRecord();
			}
		});
		
		inputPanel.add(categoryLabel);
		inputPanel.add(categoryDropdown);
		inputPanel.add(typeLabel);
		inputPanel.add(typeField);
		inputPanel.add(descLabel);
		inputPanel.add(descField);
		inputPanel.add(save);
		inputPanel.add(updateBtn);
		inputPanel.add(deleteBtn);
		
		panel.add(inputPanel);
		
		return panel;
	}

	protected void deleteRecord() {
		int selectedRow = datagrid.getSelectedRow();
		if (selectedRow < 0) {
			JOptionPane.showMessageDialog(this, "请选择需要删除的记录");
			return;
		}
		if (JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(this, "删除该类型后对应的数据记录将不再被显示，确定要删除吗？")) {
			return;
		}
		
		int id = Integer.parseInt(
				datagrid.getValueAt(selectedRow, 3).toString()
				);
		TypeBean bean = new TypeBean();
		bean.setTypeId(id);

		if (TypeDao.deleteRecord(bean) == DaoResponse.DELETE_SUCCESS) {
			clearInput();
			datagrid.clearSelection();
			loadDatagrid();
			JOptionPane.showMessageDialog(this, "删除成功");
		} else {
			JOptionPane.showMessageDialog(this, "删除失败");
		}
		checkForButtons();
	}

	protected void updateRecord() {
		int selectedRow = datagrid.getSelectedRow();
		if (selectedRow < 0) {
			JOptionPane.showMessageDialog(this, "请选择需要更新的记录");
			return;
		}
		int categoryId = Integer.parseInt(
				datagrid.getValueAt(selectedRow, 1).toString()//Stick to what id column number is
				);
		int id = Integer.parseInt(
				datagrid.getValueAt(selectedRow, 3).toString()//Stick to what id column number is
				);
		TypeBean bean = new TypeBean();
		bean.setCategoryId(categoryId);
		bean.setTypeId(id);
		bean.setTypeName(typeField.getText().trim());
		bean.setDescription(descField.getText().trim());
		
		DaoResponse dr = TypeDao.updateRecord(bean);
		if (dr == DaoResponse.UPDATE_SUCCESS) {
			clearInput();
			datagrid.clearSelection();
			loadDatagrid();
			JOptionPane.showMessageDialog(this, "更新名称/描述成功(类别暂不可更新)");
		} else if (dr == DaoResponse.EXISTED) {
			JOptionPane.showMessageDialog(this, "该名称已存在，请输入其他名称");
		} else {
			JOptionPane.showMessageDialog(this, "更新失败");
		}
		checkForButtons();
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
