package pers.bocky.finance.view.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import pers.bocky.finance.bean.CategoryBean;
import pers.bocky.finance.bean.TimeOption;
import pers.bocky.finance.bean.TypeBean;
import pers.bocky.finance.dao.BorrowDao;
import pers.bocky.finance.dao.CategoryDao;
import pers.bocky.finance.dao.ConsumeDao;
import pers.bocky.finance.dao.DepositDao;
import pers.bocky.finance.dao.LendDao;
import pers.bocky.finance.dao.ReportDao;
import pers.bocky.finance.dao.TypeDao;
import pers.bocky.finance.util.DaoResponse;
import pers.bocky.finance.view.WillBeInMainTabbed;

public class ReportPanel extends JPanel implements WillBeInMainTabbed {

	private static final long serialVersionUID = 1L;

	private final String PANEL_TYPE = "统计";
	private boolean hasMainUI;

	private List<CategoryBean> categoryDefsList;
	private Map<Integer, List<TypeBean>> typeDefsMap;
	private List<TimeOption> timeOptionsList;

	private CategoryBean selectedCategory = new CategoryBean(CategoryBean.DEPOSIT, "默认");
	private TimeOption selectedTimeOption = TimeOption.TODAY;
	private Optional<List<TypeBean>> selectedTypes;

	private JPanel panelForCheckbox;
	private JLabel resultText;
	
	private JButton calNetDepositBtn1;
	private JButton calRemainingBorrowAmountFromRelationshipBtn;
	
	public ReportPanel() {
		super();
		setStyles();
	}

	/**
	 * Styles configuration
	 */
	private void setStyles() {
		this.setLayout(new BorderLayout());
	}

	private void createUIAndLoadOnce() {
		loadDatagrid();// Init static data first
		add(createTopPanel(), BorderLayout.NORTH);
		add(createMiddlePanel(), BorderLayout.CENTER);
		add(createBottomPanel(), BorderLayout.SOUTH);
		tryCalculate();
	}

	private Component createTopPanel() {
		JPanel panelForRadios = new JPanel(new GridLayout(1, 0));
		panelForRadios.setBackground(new Color(129, 195, 230));
		ButtonGroup bg1 = new ButtonGroup();
		categoryDefsList.stream().forEach(categoryBean -> {
			JPanel panel = new JPanel();
			panel.setOpaque(false);
			JRadioButton radioButton = new JRadioButton(categoryBean.getCategoryName(),
					selectedCategory.equals(categoryBean));
			radioButton.setOpaque(false);
			panel.add(radioButton);
			panelForRadios.add(panel);
			bg1.add(radioButton);

			radioButton.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						selectedCategory = categoryBean;
						System.out.println(selectedCategory + " > " + selectedCategory.getCategoryId());
						panelForCheckbox.removeAll();
//						panelForCheckbox.invalidate();
						add(createMiddlePanel(), BorderLayout.CENTER);
						panelForCheckbox.updateUI();
//						panelForCheckbox.repaint();
						tryCalculate();
					}

				}
			});
		});
		return panelForRadios;
	}

	private void tryCalculate() {
		System.out.println("selected values:");
		System.out.println(selectedTimeOption);
		System.out.println(selectedCategory + "" + selectedCategory.getCategoryId());
		System.out.println(selectedTypes);
		System.out.println("tryCalculate selectedTypes.isPresent() > " + (selectedTypes.isPresent() ? selectedTypes.get() : "not present"));
		double result = 0;
		if (selectedTypes.isPresent() && selectedTypes.get().size() > 0) {
			Integer[] selectedTypeIds = selectedTypes.get().stream().map(bean -> bean.getTypeId()).toArray(Integer[]::new);
			switch (selectedCategory.getCategoryId()) {
			case CategoryBean.DEPOSIT:
				switch (selectedTimeOption) {
				case LATEST_30:
					result = DepositDao.calculateAmountOfLatestMonthOfType(selectedTypeIds);
					break;
				case LAST_MONTH:
					result = DepositDao.calculateAmountOfLastMonthhOfType(selectedTypeIds);
					break;
				case AVG_MONTH:
					result = DepositDao.calculateAvgMonthAmountOfType(selectedTypeIds);
					break;
				case CURRENT_MONTH:
					result = DepositDao.calculateAmountFromThisMonthOfType(selectedTypeIds);
					break;
				case LAST_WEEK:
					result = DepositDao.calculateAmountOfLastWeekOfType(selectedTypeIds);
					break;
				case CURRENT_WEEK:
					result = DepositDao.calculateAmountFromThisWeekOfType(selectedTypeIds);
					break;
				case TODAY:
					result = DepositDao.calculateAmountOfTodayOfType(selectedTypeIds);
					break;
				case AVG_WEEK:
					result = DepositDao.calculateAvgWeekAmountOfType(selectedTypeIds);
					break;
				case AVG_DAY:
					result = DepositDao.calculateAvgDayAmountOfType(selectedTypeIds);
					break;
				default:
					break;
				}
				break;
			case CategoryBean.CONSUME:
				switch (selectedTimeOption) {
				case LATEST_30:
					result = ConsumeDao.calculateAmountOfLatestMonthOfType(selectedTypeIds);
					break;
				case LAST_MONTH:
					result = ConsumeDao.calculateAmountOfLastMonthhOfType(selectedTypeIds);
					break;
				case AVG_MONTH:
					result = ConsumeDao.calculateAvgMonthAmountOfType(selectedTypeIds);
					break;
				case CURRENT_MONTH:
					result = ConsumeDao.calculateAmountFromThisMonthOfType(selectedTypeIds);
					break;
				case LAST_WEEK:
					result = ConsumeDao.calculateAmountOfLastWeekOfType(selectedTypeIds);
					break;
				case CURRENT_WEEK:
					result = ConsumeDao.calculateAmountFromThisWeekOfType(selectedTypeIds);
					break;
				case TODAY:
					result = ConsumeDao.calculateAmountOfTodayOfType(selectedTypeIds);
					break;
				case AVG_WEEK:
					result = ConsumeDao.calculateAvgWeekAmountOfType(selectedTypeIds);
					break;
				case AVG_DAY:
					result = ConsumeDao.calculateAvgDayAmountOfType(selectedTypeIds);
					break;
				default:
					break;
				}
				break;
			case CategoryBean.BORROW:
				switch (selectedTimeOption) {
				case LATEST_30:
					result = BorrowDao.calculateAmountOfLatestMonthOfType(selectedTypeIds);
					break;
				case LAST_MONTH:
					result = BorrowDao.calculateAmountOfLastMonthhOfType(selectedTypeIds);
					break;
				case AVG_MONTH:
					result = BorrowDao.calculateAvgMonthAmountOfType(selectedTypeIds);
					break;
				case CURRENT_MONTH:
					result = BorrowDao.calculateAmountFromThisMonthOfType(selectedTypeIds);
					break;
				case LAST_WEEK:
					result = BorrowDao.calculateAmountOfLastWeekOfType(selectedTypeIds);
					break;
				case CURRENT_WEEK:
					result = BorrowDao.calculateAmountFromThisWeekOfType(selectedTypeIds);
					break;
				case TODAY:
					result = BorrowDao.calculateAmountOfTodayOfType(selectedTypeIds);
					break;
				case AVG_WEEK:
					result = BorrowDao.calculateAvgWeekAmountOfType(selectedTypeIds);
					break;
				case AVG_DAY:
					result = BorrowDao.calculateAvgDayAmountOfType(selectedTypeIds);
					break;
				default:
					break;
				}
				break;
			case CategoryBean.LEND:
				switch (selectedTimeOption) {
				case LATEST_30:
					result = LendDao.calculateAmountOfLatestMonthOfType(selectedTypeIds);
					break;
				case LAST_MONTH:
					result = LendDao.calculateAmountOfLastMonthhOfType(selectedTypeIds);
					break;
				case AVG_MONTH:
					result = LendDao.calculateAvgMonthAmountOfType(selectedTypeIds);
					break;
				case CURRENT_MONTH:
					result = LendDao.calculateAmountFromThisMonthOfType(selectedTypeIds);
					break;
				case LAST_WEEK:
					result = LendDao.calculateAmountOfLastWeekOfType(selectedTypeIds);
					break;
				case CURRENT_WEEK:
					result = LendDao.calculateAmountFromThisWeekOfType(selectedTypeIds);
					break;
				case TODAY:
					result = LendDao.calculateAmountOfTodayOfType(selectedTypeIds);
					break;
				case AVG_WEEK:
					result = LendDao.calculateAvgWeekAmountOfType(selectedTypeIds);
					break;
				case AVG_DAY:
					result = LendDao.calculateAvgDayAmountOfType(selectedTypeIds);
					break;
				default:
					break;
				}
				break;

			default:
				break;
			}//end switch
		} else {
			result = 0;
		}
		resultText.setText(selectedTimeOption.getTimeOptionName() + " " + selectedCategory.getCategoryName() + ": " + result);
		if (selectedCategory.getCategoryId() == CategoryBean.CONSUME ) {
			resultText.setToolTipText("2018年5月和6月存在脏数据，不纳入统计范围.");
		} else {
			resultText.setToolTipText(null);
		}
	}

	private JPanel createMiddlePanel() {
		panelForCheckbox = new JPanel(new GridLayout(5, 0));
		panelForCheckbox.setBackground(new Color(208, 223, 239));
		//Fetch all available types for UI to display.
		selectedTypes = Optional.ofNullable(typeDefsMap.get(selectedCategory.getCategoryId()));
		List<Integer> favTypeList = ReportDao.fetchFavoriteReportOptions(selectedCategory.getCategoryId()).stream().map(bean -> bean.getTypeId()).collect(Collectors.toList());
		if (selectedTypes.isPresent()) {
			selectedTypes.get().stream().forEach(typeBean -> {
				JCheckBox checkBox = new JCheckBox(typeBean.getTypeName(), favTypeList.contains(typeBean.getTypeId()));
				checkBox.setOpaque(false);
				panelForCheckbox.add(checkBox);
				checkBox.addItemListener(new ItemListener() {
					
					@Override
					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED) {
							System.out.println(typeBean.getTypeName() + " checked");
							selectedTypes.get().add(typeBean);
						} else if (e.getStateChange() == ItemEvent.DESELECTED) {
							System.out.println(typeBean.getTypeName() + " unchecked");
							selectedTypes.get().remove(typeBean);
						}
						tryCalculate();
						//Currently, save for each time change happens.
						saveSelectedTypesForCategory(selectedCategory.getCategoryId(), selectedTypes.get());
					}
				});
			});
			//Ensure it holds the real selected/checked types.
			selectedTypes = Optional.ofNullable(selectedTypes.get().stream().filter(bean -> favTypeList.contains(bean.getTypeId())).collect(Collectors.toList()));
		}
		
		return panelForCheckbox;
	}

	protected void saveSelectedTypesForCategory(Integer categoryId, List<TypeBean> list) {
		DaoResponse dr = ReportDao.saveFavReportOptions(categoryId, list);
		if (dr == DaoResponse.SAVE_SUCCESS) {
			System.out.println("Favorite report options saved successfully.");
		} else {
			System.out.println("Failed to save favorite report options.");
		}
	}

	private JPanel createBottomPanel() {
		JPanel layoutPanel = new JPanel(new GridLayout(2, 0));

		JPanel panelForRadios2 = new JPanel(new GridLayout(1, 0));
		panelForRadios2.setBackground(new Color(204, 170, 126, 255));
		ButtonGroup bg2 = new ButtonGroup();
		timeOptionsList.stream().forEach(timeOption -> {
			JPanel panel = new JPanel();
			panel.setOpaque(false);
			JRadioButton radioButton = new JRadioButton(timeOption.getTimeOptionName(),
					timeOption == selectedTimeOption);
			radioButton.setOpaque(false);
			panel.add(radioButton);
			panelForRadios2.add(panel);
			bg2.add(radioButton);

			radioButton.addItemListener(new ItemListener() {

				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						selectedTimeOption = timeOption;
						tryCalculate();
					}
				}
			});
		});

		JPanel panelForBtn = new JPanel(new BorderLayout(150, 10));
		resultText = new JLabel();
		resultText.setForeground(Color.RED);

		calNetDepositBtn1 = new JButton("实际存款");
		calNetDepositBtn1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(ReportPanel.this, calNetDeposit(), "实际存款", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		calRemainingBorrowAmountFromRelationshipBtn = new JButton("剩余欠款");
		calRemainingBorrowAmountFromRelationshipBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(ReportPanel.this, calRemainingBorrowAmountFromRelationship(), "剩余欠款", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		panelForBtn.add(calNetDepositBtn1, BorderLayout.WEST);
		panelForBtn.add(resultText, BorderLayout.CENTER);
		panelForBtn.add(calRemainingBorrowAmountFromRelationshipBtn, BorderLayout.EAST);
		
		layoutPanel.add(panelForRadios2);
		layoutPanel.add(panelForBtn);

		return layoutPanel;
	}

	@Override
	public void loadDatagrid() {
		if (hasMainUI) {
			tryCalculate();
		} else {
			categoryDefsList = loadAllCategoryDefs();
			typeDefsMap = loadAllTypes();
			timeOptionsList = loadAllTimeOptions();
		}
	}

	private List<TimeOption> loadAllTimeOptions() {
		List<TimeOption> list = new ArrayList<>();
		list.add(TimeOption.TODAY);
		list.add(TimeOption.AVG_DAY);
		list.add(TimeOption.LAST_WEEK);
		list.add(TimeOption.CURRENT_WEEK);
		list.add(TimeOption.AVG_WEEK);
		list.add(TimeOption.LATEST_30);
		list.add(TimeOption.AVG_MONTH);
		list.add(TimeOption.LAST_MONTH);
		list.add(TimeOption.CURRENT_MONTH);
		return list;
	}

	private Map<Integer, List<TypeBean>> loadAllTypes() {
		List<TypeBean> list = TypeDao.fetchAllTypesAndCategories();
		return list.stream().collect(Collectors.groupingBy(typeBean -> typeBean.getCategoryId()));
	}

	private List<CategoryBean> loadAllCategoryDefs() {
		return CategoryDao.fetchAllCategories();
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

	private Object calRemainingBorrowAmountFromRelationship() {
		return BorrowDao.calAllBorrowAmountOfType(TypeBean.BORROW_FROM_RELATIONSHIPS) - BorrowDao.calAllBorrowHistoryAmountOfType(TypeBean.BORROW_FROM_RELATIONSHIPS);
	}

	private double calNetDeposit() {
		return ((double) Math.round(
				(DepositDao.calculateAllDepositRecsAmount() 
				- ConsumeDao.calculateAmountOfType(
						TypeBean.CONSUME_FOR_PAY_LOAN, 
						TypeBean.CONSUME_FOR_PAY_RELATIONSHIPS,
						TypeBean.CONSUME_COSTING_DEPOSIT
						)
				) * 100)) / 100;
	}
	
}
