package pers.bocky.finance.view.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
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
import pers.bocky.finance.dao.TypeDao;
import pers.bocky.finance.view.WillBeInMainTabbed;

public class ReportPanel extends JPanel implements WillBeInMainTabbed {

	private static final long serialVersionUID = 1L;

	private final String PANEL_TYPE = "统计";
	private boolean hasMainUI;

	private List<CategoryBean> categoryDefsList;
	private Map<Integer, List<TypeBean>> typeDefsMap;
	private List<TimeOption> timeOptionsList;

	private CategoryBean selectedCategory = new CategoryBean(1, "默认");
	private TimeOption selectedTimeOption = TimeOption.LATEST_30;
	private Optional<List<TypeBean>> selectedTypes;

	private JPanel panelForCheckbox;
	private JLabel resultText;
	
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
		add(createMiddlePanel(false), BorderLayout.CENTER);
		add(createBottomPanel(), BorderLayout.SOUTH);
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
						add(createMiddlePanel(false), BorderLayout.CENTER);
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
		System.out.println("tryCalculate > " + selectedTypes.isPresent() + ", " + (selectedTypes.isPresent() ? selectedTypes.get() : "not present"));
		if (selectedTypes.isPresent() && selectedTypes.get().size() > 0) {
			System.out.println("none null");
			System.out.println("selected values:");
			System.out.println(selectedTimeOption);
			System.out.println(selectedCategory + "" + selectedCategory.getCategoryId());
			System.out.println(selectedTypes);

			Integer[] selectedTypeIds = selectedTypes.get().stream().map(bean -> bean.getTypeId()).toArray(Integer[]::new);
			double result = 0;
			switch (selectedCategory.getCategoryId()) {
			case 1://deposit
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
				default:
					break;
				}
				break;
			case 2://consume
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
				default:
					break;
				}
				break;
			case 3://borrow
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
				default:
					break;
				}
				break;
			case 4://lend
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
				default:
					break;
				}
				break;

			default:
				break;
			}//end switch
			
			//JOptionPane.showMessageDialog(null, "Result: " + result + (selectedCategory.getCategoryId() == 2 ? "\nNote: 2018.5 and 6 and current month are not taken into account." : ""), selectedTimeOption.getTimeOptionName() + " For " + selectedCategory, JOptionPane.INFORMATION_MESSAGE);
//		}
			
			resultText.setText(selectedTimeOption.getTimeOptionName() + " for " + selectedCategory.getCategoryName() + ": " + result);
		}//end if
	}

	private JPanel createMiddlePanel(boolean allChecked) {
		panelForCheckbox = new JPanel(new GridLayout(5, 0));
		panelForCheckbox.setBackground(new Color(208, 223, 239));
		selectedTypes = Optional.ofNullable(typeDefsMap.get(selectedCategory.getCategoryId()));
		if (selectedTypes.isPresent()) {
			selectedTypes.get().stream().forEach(typeBean -> {
				JCheckBox checkBox = new JCheckBox(typeBean.getTypeName(), allChecked);
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
					}
				});
			});
			if (!allChecked) {
				selectedTypes.get().clear();
			}
		}
		
		return panelForCheckbox;
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
						System.out.println(timeOption.toString());
						selectedTimeOption = timeOption;
					}
					tryCalculate();
				}
			});
		});

		JPanel panelForBtn = new JPanel();
		resultText = new JLabel();
		resultText.setForeground(Color.RED);
		panelForBtn.add(resultText);

		layoutPanel.add(panelForRadios2);
		layoutPanel.add(panelForBtn);

		return layoutPanel;
	}

	@Override
	public void loadDatagrid() {
		categoryDefsList = loadAllCategoryDefs();
		typeDefsMap = loadAllTypes();
		timeOptionsList = loadAllTimeOptions();
	}

	private List<TimeOption> loadAllTimeOptions() {
		List<TimeOption> list = new ArrayList<>();
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

}
