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
import java.text.NumberFormat;
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

import pers.bocky.finance.bean.DepositBean;
import pers.bocky.finance.bean.TypeBean;
import pers.bocky.finance.component.DataGrid;
import pers.bocky.finance.component.DateField;
import pers.bocky.finance.component.FilterPanel;
import pers.bocky.finance.dao.DepositDao;
import pers.bocky.finance.dao.TypeDao;
import pers.bocky.finance.listener.ButtonActionListener;
import pers.bocky.finance.listener.MyDocument;
import pers.bocky.finance.util.DaoResponse;
import pers.bocky.finance.util.DateUtil;
import pers.bocky.finance.view.WillBeInMainTabbed;

public class DepositPanel extends JPanel implements WillBeInMainTabbed{

	private static final long serialVersionUID = 1L;

	private final String PANEL_TYPE = "存款";
	private DataGrid datagrid;
	private ButtonActionListener listener;
	private JTextField sourceField;
	private JTextField amountField;
	private JTextField descField;
	private JComboBox<TypeBean> typesDropdown;
	private JButton updateBtn;
	private JButton deleteBtn;
	private DateField dp;

	private boolean hasMainUI;
	
	public DepositPanel() {
		super();
		listener = new ButtonActionListener(this);
		setStyles();
	}

	/**
	 * Styles configuration
	 */
	private void setStyles() {
		this.setLayout(new BorderLayout());
		this.setBackground(Color.BLACK);
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
		
		panel.add(new FilterPanel(this, DepositBean.CATEGORY_ID));
		
		return panel;
	}

	private JScrollPane createMiddlePanel() {
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(new Color(199, 237, 204, 255));
		
		final String[] COL_NAMES = {"ID", "类型 ID", "类型", "来源", "数量", "备注", "发生时间", "最后更新于", "创建时间"};
		datagrid = new DataGrid(COL_NAMES, new String[] {"ID", "类型 ID"}
		, new String[] {"类型", "来源", "数量"}, new String[] {"备注"});
		
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

	protected void checkForButtons() {
		if (datagrid.getSelectedRow() == -1) {
			updateBtn.setEnabled(false);
			deleteBtn.setEnabled(false);
		} else {
			updateBtn.setEnabled(true);
			deleteBtn.setEnabled(true);
		}
	}

	protected void fillFields(DataGrid datagrid, int selectedRowIndex) {
		if (datagrid != null && selectedRowIndex > -1) {
			String typeId = (String) datagrid.getValueAt(selectedRowIndex, 1);
			String source = (String) datagrid.getValueAt(selectedRowIndex, 3);
			String amount = (String) datagrid.getValueAt(selectedRowIndex, 4);
			String desc = (String) datagrid.getValueAt(selectedRowIndex, 5);
			String occurTs = (String) datagrid.getValueAt(selectedRowIndex, 6);
			
			typesDropdown.setSelectedIndex(getDropdownIndexByTypeId(Integer.parseInt(typeId)));
			sourceField.setText(source);
			amountField.setText(amount.replaceAll(",", ""));
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
		savebtn.setActionCommand("saveDepositRecord");
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
		
		JLabel sourceLabel = new JLabel("来源");
		sourceField = new JTextField();
		JLabel amountLabel = new JLabel("数量");
		amountField = new JTextField();
		
		MyDocument mm = new MyDocument(10, savebtn, sourceField, amountField);
		sourceField.setDocument(mm);
		sourceField.getDocument().addDocumentListener(mm);
		amountField.setDocument(new MyDocument(true, 9));//123456.89
		amountField.getDocument().addDocumentListener(mm);
		
		JLabel descLabel = new JLabel("备注");
		descField = new JTextField();
		
		JLabel occurTsLabel = new JLabel("实际发生时间(yyyy MM dd)");
		dp = new DateField();
		
		JButton calBtn = new JButton("求和");
		JLabel sumLabel = new JLabel("");
		sumLabel.setForeground(Color.RED);
		calBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				sumLabel.setText(NumberFormat.getNumberInstance().format(sumAmount()));
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
		inputPanel.add(sourceField);
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
		int depositId = Integer.parseInt(
				datagrid.getValueAt(selectedRow, 0).toString()
				);
		DepositBean bean = new DepositBean();
		bean.setDepositId(depositId);
		bean.setTypeId(((TypeBean)typesDropdown.getSelectedItem()).getTypeId());
		bean.setSource(sourceField.getText().trim());
		bean.setAmount(Double.parseDouble(amountField.getText().trim()));
		bean.setDescription(descField.getText().trim());
		bean.setOccurTs(dp.getResultAsTimestamp());
		if (DepositDao.updateRecord(bean) == DaoResponse.UPDATE_SUCCESS) {
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
		DepositBean bean = new DepositBean();
		bean.setDepositId(id);

		if (DepositDao.deleteRecord(bean) == DaoResponse.DELETE_SUCCESS) {
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
			double amount = Double.parseDouble(amountStr.replaceAll(",", ""));
			doubleList.add(amount);
		}
		
		return doubleList.stream().reduce((acc, ele) -> acc += ele).orElse(0d);
	}

	/**
	 * 
	 */
	private void loadTypeDropDown() {
		List<TypeBean> list = TypeDao.fetchTypeBy(DepositBean.CATEGORY_ID, null);
		final TypeBean[] actions = list.toArray(new TypeBean[0]);
		typesDropdown.setModel(new DefaultComboBoxModel<TypeBean>(actions));
	}

	@Override
	public void loadDatagrid() {
		List<DepositBean> list = DepositDao.fetchAllDepositRecs();
		loadDatagrid(list);
	}
	
	public boolean loadDatagrid(List<DepositBean> list) {
		if (list == null) {
			return false;
		}
		List<Vector<String>> dataVectorList = new ArrayList<Vector<String>>();
		for (int i = 0; i < list.size(); i++) {
			DepositBean bean = list.get(i);
			Vector<String> v = new Vector<String>();
			v.add(bean.getDepositId().toString());
			v.add(bean.getTypeId().toString());
			v.add(bean.getTypeName());
			v.add(bean.getSource());
			v.add(NumberFormat.getNumberInstance().format(bean.getAmount()));
			v.add(bean.getDescription());
			v.add(DateUtil.timestamp2Str(bean.getOccurTs()));
			v.add(DateUtil.timestamp2Str(bean.getLastUpdateTs()));
			v.add(DateUtil.timestamp2Str(bean.getAddTs()));
			dataVectorList.add(v);
		}
		datagrid.setData(dataVectorList);
		return true;
	}

	public DepositBean formSaveBean() {
		DepositBean bean = new DepositBean();
		bean.setTypeId(
				Integer.parseInt(
						((TypeBean)typesDropdown.getSelectedItem()).getTypeId().toString()
				)
		);
		bean.setSource(sourceField.getText().trim());
		bean.setAmount(Double.parseDouble(amountField.getText().trim()));
		bean.setDescription(descField.getText().trim());
		bean.setOccurTs(dp.getResultAsTimestamp());
		System.out.println("The formed deposit to save bean > " + bean);
		return bean;
	}
	
	public void clearInput() {
		sourceField.setText("");
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
