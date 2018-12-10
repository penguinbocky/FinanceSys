package pers.bocky.finance.view.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
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

import pers.bocky.finance.bean.ConsumeBean;
import pers.bocky.finance.bean.TypeBean;
import pers.bocky.finance.component.DataGrid;
import pers.bocky.finance.component.DateField;
import pers.bocky.finance.component.FilterPanel;
import pers.bocky.finance.dao.ConsumeDao;
import pers.bocky.finance.dao.TypeDao;
import pers.bocky.finance.listener.ButtonActionListener;
import pers.bocky.finance.listener.MyDocument;
import pers.bocky.finance.util.DaoResponse;
import pers.bocky.finance.util.DateUtil;
import pers.bocky.finance.util.NumberUtil;
import pers.bocky.finance.util.StringUtil;
import pers.bocky.finance.view.WillBeInMainTabbed;

public class ConsumePanel extends JPanel implements WillBeInMainTabbed{

	private static final long serialVersionUID = 1L;

	private final String PANEL_TYPE = "支出";
	private DataGrid datagrid;
	private ButtonActionListener listener;
	private JTextField destField;
	private JTextField amountField;
	private JTextField descField;
	private JComboBox<TypeBean> typesDropdown;
	private JButton updateBtn;
	private JButton deleteBtn;
	private DateField dp;

	private boolean hasMainUI;
	
	public ConsumePanel() {
		super();
		listener = new ButtonActionListener(this);
		setStyles();
	}

	/**
	 * Styles configuration
	 */
	private void setStyles() {
		this.setLayout(new BorderLayout());
	}

	private void createUIAndLoadOnce() {
		add(createTopPanel(), BorderLayout.NORTH);
		add(createMiddlePanel(), BorderLayout.CENTER);
		add(createBottomPanel(), BorderLayout.SOUTH);
		loadDatagrid();
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

		panel.add(new FilterPanel(this, ConsumeBean.CATEGORY_ID));
		
		return panel;
	}

	private JScrollPane createMiddlePanel() {
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(new Color(199, 237, 204, 255));
		
		final String[] COL_NAMES = {"ID", "类型 ID", "类型", "去向", "数量", "备注", "发生时间", "最后更新于", "创建时间"};
		datagrid = new DataGrid(COL_NAMES);
		
		JPanel p1 = new JPanel();
		p1.add(new JLabel("更新"));
		p1.setForeground(Color.CYAN);
		JPanel p2 = new JPanel();
		p2.add(new JLabel("删除"));
		p2.setForeground(Color.MAGENTA);
//		JList<String> items = new JList<String>(new String[]{new String("更新"), new String("删除")});
//		PopupFactory popupFactory = PopupFactory.getSharedInstance();
		datagrid.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				checkForButtons();
				int selectedRowIndex = datagrid.getSelectedRow();
				if (selectedRowIndex != -1) {
					if (e.getButton() == MouseEvent.BUTTON1) {
						fillFields(datagrid, selectedRowIndex);
					} else if (e.getButton() == MouseEvent.BUTTON3) {
//						Popup pop = popupFactory.getPopup(datagrid, items, e.getXOnScreen(), e.getYOnScreen());
//						pop.show();
//						
//						String depositId = datagrid.getValueAt(selectedRowIndex, 0).toString();
//						System.out.println("depositId > " + depositId);
					}
				}
			}
			
		});
		scrollPane.setViewportView(datagrid);
//		loadDatagrid();
		
		return scrollPane;
	}

	protected void fillFields(DataGrid datagrid, int selectedRowIndex) {
		if (datagrid != null && selectedRowIndex > -1) {
			String typeId = (String) datagrid.getValueAt(selectedRowIndex, 1);
			String dest = (String) datagrid.getValueAt(selectedRowIndex, 3);
			String amount = (String) datagrid.getValueAt(selectedRowIndex, 4);
			String desc = (String) datagrid.getValueAt(selectedRowIndex, 5);
			String occurTs = (String) datagrid.getValueAt(selectedRowIndex, 6);
			
			typesDropdown.setSelectedIndex(getDropdownIndexByTypeId(Integer.parseInt(typeId)));
			destField.setText(dest);
			amountField.setText(amount);
			descField.setText(desc);
			if (occurTs != null && !"".equals(occurTs)) {
				dp.fillFields(occurTs.substring(0, 4), occurTs.substring(5, 7), occurTs.substring(8, 10));
			} else {
				dp.fillFields("", "", "");
			}
		}
	}
	
	private int getDropdownIndexByTypeId(int typeId) {
		int size = typesDropdown.getItemCount();
		for (int i = 0; i < size; i++) {
			TypeBean bean = typesDropdown.getItemAt(i);
			if (bean.getTypeId() == typeId) {
				return i;
			}
		}
		return -1;
	}
	
	private JPanel createBottomPanel() {
		JPanel panel = new JPanel();
		JPanel inputPanel = new JPanel(new GridLayout(0, 4, 20, 0));
		panel.setBackground(new Color(204, 170, 126, 255));
		inputPanel.setOpaque(false);
		
		JButton savebtn = new JButton("新增");
		savebtn.setEnabled(false);
		savebtn.setActionCommand("saveConsumeRecord");
		savebtn.addActionListener(listener);
		
		JLabel typeLabel = new JLabel("类型[点击刷新]");
		typeLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		typeLabel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				loadTypeDropDown();
			}
			
		});
		typesDropdown = new JComboBox<TypeBean>();
		loadTypeDropDown();
		
		JLabel sourceLabel = new JLabel("去向");
		destField = new JTextField();
		JLabel amountLabel = new JLabel("数量");
		amountField = new JTextField();
		
		MyDocument mm = new MyDocument(10, savebtn, destField, amountField);
		destField.setDocument(mm);
		destField.getDocument().addDocumentListener(mm);
		amountField.setDocument(new MyDocument(true, 9));//123456.89
		amountField.getDocument().addDocumentListener(mm);
		
		JLabel descLabel = new JLabel("备注");
		descField = new JTextField();
		
		JLabel occurTsLabel = new JLabel("实际发生时间");
		dp = new DateField();
		
		JButton calBtn = new JButton("求和");
		JLabel sumLabel = new JLabel("");
		sumLabel.setForeground(Color.RED);
		calBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				sumLabel.setText(StringUtil.subZeroAndDot(sumAmount()));
			}
		});
		
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
		
		inputPanel.add(typeLabel);
		inputPanel.add(typesDropdown);
		inputPanel.add(sourceLabel);
		inputPanel.add(destField);
		inputPanel.add(amountLabel);
		inputPanel.add(amountField);
		inputPanel.add(descLabel);
		inputPanel.add(descField);
		inputPanel.add(occurTsLabel);
		inputPanel.add(dp);
		inputPanel.add(savebtn);
		inputPanel.add(updateBtn);
		inputPanel.add(deleteBtn);
		
		inputPanel.add(calBtn);
		inputPanel.add(sumLabel);
		
		
		panel.add(inputPanel);
		
		return panel;
	}

	protected void updateRecord() {
		int selectedRow = datagrid.getSelectedRow();
		if (selectedRow < 0) {
			JOptionPane.showMessageDialog(this, "请选择需要更新的记录");
			return;
		}
		int id = Integer.parseInt(
				datagrid.getValueAt(selectedRow, 0).toString()
				);
		ConsumeBean bean = new ConsumeBean();
		bean.setConsumeId(id);
		bean.setTypeId(((TypeBean)typesDropdown.getSelectedItem()).getTypeId());
		bean.setDest(destField.getText().trim());
		bean.setAmount(Double.parseDouble(amountField.getText().trim()));
		bean.setDescription(descField.getText().trim());
		bean.setOccurTs(dp.getResultAsTimestamp());
		if (ConsumeDao.updateRecord(bean) == DaoResponse.UPDATE_SUCCESS) {
			clearInput();
			datagrid.clearSelection();
			loadDatagrid();
			JOptionPane.showMessageDialog(this, "更新成功");
		} else {
			JOptionPane.showMessageDialog(this, "更新失败");
		}
		checkForButtons();
	}
	
	protected void deleteRecord() {
		int selectedRow = datagrid.getSelectedRow();
		if (selectedRow < 0) {
			JOptionPane.showMessageDialog(this, "请选择需要删除的记录");
			return;
		}
		if (JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(this, "确定要删除吗？")) {
			return;
		}
		
		int id = Integer.parseInt(
				datagrid.getValueAt(selectedRow, 0).toString()
				);
		ConsumeBean bean = new ConsumeBean();
		bean.setConsumeId(id);

		if (ConsumeDao.deleteRecord(bean) == DaoResponse.DELETE_SUCCESS) {
			clearInput();
			datagrid.clearSelection();
			loadDatagrid();
			JOptionPane.showMessageDialog(this, "删除成功");
		} else {
			JOptionPane.showMessageDialog(this, "删除失败");
		}
		checkForButtons();
	}
	
	protected double sumAmount() {
		if (datagrid == null || datagrid.getModel().getRowCount() < 1) {
			return 0;
		}
		int size = datagrid.getModel().getRowCount();
		List<Double> doubleList = new ArrayList<Double>();
		for (int row = 0; row < size; row++) {
			String amountStr = (String) datagrid.getValueAt(row, 4);
			double amount = Double.parseDouble(amountStr);
			doubleList.add(amount);
		}
		
		return NumberUtil.sum(doubleList);
	}
	
	/**
	 * 
	 */
	private void loadTypeDropDown() {
		List<TypeBean> list = TypeDao.fetchTypeByCategory(ConsumeBean.CATEGORY_ID);
		final TypeBean[] actions = list.toArray(new TypeBean[0]);
		typesDropdown.setModel(new DefaultComboBoxModel<TypeBean>(actions));
	}

	@Override
	public void loadDatagrid() {
		List<ConsumeBean> list = ConsumeDao.fetchAllConsumeRecs();
		loadDatagrid(list);
	}

	public boolean loadDatagrid(List<ConsumeBean> list) {
		if (list == null) {
			return false;
		}
		List<Vector<String>> dataVectorList = new ArrayList<Vector<String>>();
		for (int i = 0; i < list.size(); i++) {
			ConsumeBean bean = list.get(i);
			Vector<String> v = new Vector<String>();
			v.add(bean.getConsumeId().toString());
			v.add(bean.getTypeId().toString());
			v.add(bean.getTypeName());
			v.add(bean.getDest());
			v.add(StringUtil.subZeroAndDot(bean.getAmount()));
			v.add(bean.getDescription());
			v.add(bean.getOccurTs() != null ? DateUtil.timestamp2DateStr(bean.getOccurTs()) : null);
			v.add(bean.getLastUpdateTs().toString());
			v.add(bean.getAddTs().toString());
			dataVectorList.add(v);
		}
		datagrid.setData(dataVectorList);
		return true;
	}
	
	public ConsumeBean formSaveBean() {
		ConsumeBean bean = new ConsumeBean();
		bean.setTypeId(
				Integer.parseInt(
						((TypeBean)typesDropdown.getSelectedItem()).getTypeId().toString()
				)
		);
		bean.setDest(destField.getText().trim());
		bean.setAmount(Double.parseDouble(amountField.getText().trim()));
		bean.setDescription(descField.getText().trim());
		bean.setOccurTs(dp.getResultAsTimestamp());
		System.out.println("The formed deposit to save bean > " + bean);
		return bean;
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
	
	public void clearInput() {
		destField.setText("");
		amountField.setText("");
		descField.setText("");
		checkForButtons();
	}
	
	@Override
	public String getTabTitle() {
		return PANEL_TYPE;
	}

	@Override
	public void createMainUI() {
		createUIAndLoadOnce();
		hasMainUI = true;
	}

	@Override
	public boolean hasMainUI() {
		return hasMainUI;
	}

}
