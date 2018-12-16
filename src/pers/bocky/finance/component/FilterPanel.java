package pers.bocky.finance.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pers.bocky.finance.bean.ConsumeBean;
import pers.bocky.finance.bean.DepositBean;
import pers.bocky.finance.bean.FilterBean;
import pers.bocky.finance.bean.TypeBean;
import pers.bocky.finance.dao.ConsumeDao;
import pers.bocky.finance.dao.DepositDao;
import pers.bocky.finance.dao.TypeDao;
import pers.bocky.finance.view.WillBeInTabbed;
import pers.bocky.finance.view.impl.ConsumePanel;
import pers.bocky.finance.view.impl.DepositPanel;

public class FilterPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	// A dropdown displaying all available filters which are the same as (may less than)
	// datagrid columns
	private JComboBox<FilterBean> availableFiltersDropdown;

	//A dropdown displaying all available compare way.
	private JComboBox<Comparator> comparatorDropdown;
	
	// A kind of filter value standing for string/number
	private JTextField filterValueOfStrNum;
	
	// Another kind of filter value standing for pickable time
	private com.eltima.components.ui.DatePicker filterValueOfDateTime;
	
	// Another kind of filter value standing for pickable time
	private com.eltima.components.ui.DatePicker filterValueOfDateTime2;
	
	// Another kind of filter value standing for types
	private JComboBox<TypeBean> filterValueOfTypes;

	private JButton okBtn;
	
	private FilterBean lastFilterBean;
	
	private Comparator lastComparator;
	
	//Category ID for 4 tab views.
	private Integer categoryId;
	
	private WillBeInTabbed panel;
	
	public FilterPanel(WillBeInTabbed panel, Integer categoryId) {
		super();
		this.panel = panel;
		this.categoryId = categoryId;
		initUI();
	}

	private void initUI() {
		initAvailableFilters();
		initOkBtn();
		configFilter();
		setOpaque(false);
	}

	private void initOkBtn() {
		okBtn = new JButton("确定");
		okBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String filterType= ((FilterBean) availableFiltersDropdown.getSelectedItem()).getType();
				String filterName = ((FilterBean) availableFiltersDropdown.getSelectedItem()).getFilterName();
				String _filterValue = null;
				String _filterValue2 = null;
				if (filterType.equalsIgnoreCase("date")) {
					//No need to judge datepicker is null, it cannot be.
					_filterValue = "" + ((Date) filterValueOfDateTime.getValue()).getTime();
					if (lastComparator.compareTo(Comparator.介于) == 0) {
						_filterValue2 = "" + ((Date) filterValueOfDateTime2.getValue()).getTime();
					}
				} else if (filterType.equalsIgnoreCase("type")) {
					_filterValue = "" + ((TypeBean) filterValueOfTypes.getSelectedItem()).getTypeId();
				} else {
					_filterValue = filterValueOfStrNum.getText().trim();
				}
				
				System.out.println(FilterPanel.class + " >>> ");
				System.out.println("categoryId > " + categoryId);
				System.out.println("filterType > " + filterType);
				System.out.println("filterName > " + filterName);
				System.out.println("_filterValue > " + _filterValue);
				System.out.println("comparator > " + (Comparator) comparatorDropdown.getSelectedItem());
				
				switch (categoryId) {
				case DepositBean.CATEGORY_ID:
					((DepositPanel) panel).loadDatagrid(DepositDao.getRecByFilter(filterName, (Comparator) comparatorDropdown.getSelectedItem(), _filterValue, _filterValue2));
					break;
				case ConsumeBean.CATEGORY_ID:
					((ConsumePanel) panel).loadDatagrid(ConsumeDao.getRecByFilter(filterName, (Comparator) comparatorDropdown.getSelectedItem(), _filterValue, _filterValue2));				
					break;
//				case LendBean.CATEGORY_ID:
//					((LendPanel) panel).loadDatagrid(DepositDao.getRecByFilter(filterName, (Comparator) comparatorDropdown.getSelectedItem(), _filterValue));
//					break;
//				case BorrowBean.CATEGORY_ID:
//					((BorderPane) panel).loadDatagrid(DepositDao.getRecByFilter(filterName, (Comparator) comparatorDropdown.getSelectedItem(), _filterValue));
//					break;

				default:
					break;
				}
			}
		});
	}

	private void configComparator() {
		FilterBean selectedFilter = (FilterBean) availableFiltersDropdown.getSelectedItem();
		Comparator[] comparators = new Comparator[] {Comparator.等于};//Default comparator
		if (selectedFilter.getType().equalsIgnoreCase("date")) {
			comparators = new Comparator[] {
					Comparator.大于,
					Comparator.小于,
					Comparator.等于,
					Comparator.不等于,
					Comparator.介于
			};
		} else if (selectedFilter.getType().equalsIgnoreCase("type")) {
			comparators = new Comparator[] {
					Comparator.等于,
					Comparator.不等于
			};
		} else if (selectedFilter.getType().equalsIgnoreCase("number")) {
			comparators = new Comparator[] {
					Comparator.大于,
					Comparator.小于,
					Comparator.等于,
					Comparator.不等于
			};
		} else if (selectedFilter.getType().equalsIgnoreCase("string")) {
			comparators = new Comparator[] {
					Comparator.包含,
					Comparator.等于,
					Comparator.不等于
			};
		}
		
		//Do not change reference once being initiated to save the memory
		if (comparatorDropdown == null) {
			comparatorDropdown = new JComboBox<Comparator>();
			comparatorDropdown.addItemListener(new ItemListener() {
				
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						System.out.println(e.getStateChange() + ", " + e.getItem());
						Comparator b = (Comparator) e.getItem();
						if (b.compareTo(lastComparator) != 0) {
							lastComparator = b;//Must be first done in this block
							System.out.println(b);
							System.out.println(lastComparator);
							removeAll();
							configFilter();//lastComparator will be used
						}
					}
				}
			});
			
			lastComparator = comparators[0];
		}
		comparatorDropdown.setModel(new DefaultComboBoxModel<Comparator>(comparators));
		comparatorDropdown.setSelectedItem(lastComparator);//comparators is re-new every time, so to manually set it
	}

	private void configFilter() {
		configComparator();
		
		FilterBean selectedFilter = (FilterBean) availableFiltersDropdown.getSelectedItem();
		add(availableFiltersDropdown);
		add(comparatorDropdown);
		if (selectedFilter.getType().equalsIgnoreCase("date")) {
			Date tempDate = new Date();
			if (lastComparator.compareTo(Comparator.介于) == 0) {
				if ("发生时间".equals(selectedFilter.getFilterName())) {
					filterValueOfDateTime = getDatePicker(
							new Date(tempDate.getYear(), tempDate.getMonth(), tempDate.getDate() - 1), false);
					filterValueOfDateTime2 = getDatePicker(
							new Date(tempDate.getYear(), tempDate.getMonth(), tempDate.getDate()), false);
				} else {
					filterValueOfDateTime = getDatePicker(new Date(tempDate.getTime() - 24 * 3600 * 1000), true);
					filterValueOfDateTime2 = getDatePicker(tempDate, true);
				}
				add(filterValueOfDateTime);
				add(new JLabel(" --到-- "));
				add(filterValueOfDateTime2);
			} else {
				if ("发生时间".equals(selectedFilter.getFilterName())) {
					filterValueOfDateTime = getDatePicker(
							new Date(tempDate.getYear(), tempDate.getMonth(), tempDate.getDate()), false);
				} else {
					filterValueOfDateTime = getDatePicker(tempDate, true);
				}
				add(filterValueOfDateTime);
			}
		} else if (selectedFilter.getType().equalsIgnoreCase("type")) {
			filterValueOfTypes = new JComboBox<TypeBean>();
			List<TypeBean> list = TypeDao.fetchTypeBy(categoryId, null);
			final TypeBean[] actions = list.toArray(new TypeBean[0]);
			filterValueOfTypes.setModel(new DefaultComboBoxModel<TypeBean>(actions));
			add(filterValueOfTypes);
//			remove(comparator);
		}else {
			filterValueOfStrNum = new JTextField();
			filterValueOfStrNum.setPreferredSize(new Dimension(100, 25));
			add(filterValueOfStrNum);
		}
		add(okBtn);
	}

	private void initAvailableFilters() {
		List<FilterBean> filterList = initFilterList();
		if (filterList == null || filterList.size() < 1) {
			throw new Error("None filters passed in.");
		}
		availableFiltersDropdown = new JComboBox<FilterBean>();

		final FilterBean[] actions = filterList.toArray(new FilterBean[0]);
		availableFiltersDropdown.setModel(new DefaultComboBoxModel<FilterBean>(actions));
		lastFilterBean = filterList.get(0);
		availableFiltersDropdown.setSelectedIndex(0);
		availableFiltersDropdown.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					System.out.println(e.getStateChange() + ", " + e.getItem());
					FilterBean b = (FilterBean) e.getItem();
					if (!b.getFilterName().equals(lastFilterBean.getFilterName())) {
						removeAll();
						configFilter();
						lastFilterBean = b;
					}
				}
			}
		});
	}

	private List<FilterBean> initFilterList() {
		List<FilterBean> filterList = new ArrayList<FilterBean>();
		FilterBean b = null;
		int id = 0;
		if (categoryId == DepositBean.CATEGORY_ID
				|| categoryId == ConsumeBean.CATEGORY_ID) {
			b = new FilterBean();
			b.setFilterId(id++);
			b.setType("type");
			b.setFilterName("类型");
			filterList.add(b);
			
			b = new FilterBean();
			b.setFilterId(id++);
			b.setType("String");
			b.setFilterName(categoryId == DepositBean.CATEGORY_ID ? "来源" : "去向");
			filterList.add(b);
			
			b = new FilterBean();
			b.setFilterId(id++);
			b.setType("number");
			b.setFilterName("数量");
			filterList.add(b);
			
			b = new FilterBean();
			b.setFilterId(id++);
			b.setType("String");
			b.setFilterName("备注");
			filterList.add(b);
			
			b = new FilterBean();
			b.setFilterId(id++);
			b.setType("date");
			b.setFilterName("创建时间");
			filterList.add(b);
			
			b = new FilterBean();
			b.setFilterId(id++);
			b.setType("date");
			b.setFilterName("发生时间");
			filterList.add(b);
			
			b = new FilterBean();
			b.setFilterId(id++);
			b.setType("date");
			b.setFilterName("最后更新于");
			filterList.add(b);
		}
		
		return filterList;
	}
	
	private com.eltima.components.ui.DatePicker getDatePicker(Date initDate, boolean showTime) {
		final com.eltima.components.ui.DatePicker datepick;
		Font font = new Font("Times New Roman", Font.BOLD, 14);
		Dimension dimension = new Dimension(177, 24);
		int[] hilightDays = { 1, 3, 5, 7 };
		int[] disabledDays = {};
		StringBuilder defaultFormat = new StringBuilder("yyyy-MM-dd");
		if (showTime) {
			defaultFormat.append(" HH:mm:ss");
		}
		datepick = new com.eltima.components.ui.DatePicker(initDate, defaultFormat.toString(), font, dimension);
		datepick.setLocation(137, 83);
		/*
		 * //也可用setBounds()直接设置大小与位置 datepick.setBounds(137, 83, 177, 24);
		 */
		datepick.setHightlightdays(hilightDays, Color.red);
		datepick.setDisableddays(disabledDays);
		datepick.setLocale(Locale.CHINA);
		datepick.setTimePanleVisible(showTime);
		return datepick;
	}

}
