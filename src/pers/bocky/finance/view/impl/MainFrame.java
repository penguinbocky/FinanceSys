package pers.bocky.finance.view.impl;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.FontUIResource;

import pers.bocky.finance.util.PropertiesUtil;
import pers.bocky.finance.view.WillBeInMainTabbed;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private final int BASE_WIDTH = 1098;
	private final int BASE_HEIGHT = 640;
	private final int BASE_FONT_SIZE = 14;
	private final Dimension d;
	private final int WIDTH;
	private final int HEIGHT;
	private final Font GLOBAL_FONT;
	
	{
		d = Toolkit.getDefaultToolkit().getScreenSize();
		WIDTH = (int) (BASE_WIDTH * d.getWidth() / 1366);
		HEIGHT = (int) (BASE_HEIGHT * d.getHeight() / 768);
		
		GLOBAL_FONT = new Font("Microsoft Yahei", Font.PLAIN, (int) (BASE_FONT_SIZE * d.getHeight() / 640));
	}
	
	public MainFrame() throws HeadlessException {
		super();
		
		setTitle("财务管理系统");
		setBounds((int)(d.getWidth() - WIDTH) / 2, (int)(d.getHeight() - HEIGHT) / 2, 
				WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);	
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		setResizable(true);
		
		List<WillBeInMainTabbed> tabbedPanels = new ArrayList<WillBeInMainTabbed>();
		tabbedPanels.add(getConsumePanel());
		tabbedPanels.add(getDepositPanel());
		tabbedPanels.add(getBorrowPanel());
		tabbedPanels.add(getLendPanel());
		tabbedPanels.add(getReadMePanel());
		tabbedPanels.add(new MorePanel());
		tabbedPanels.add(getReportPanel());
		
		getContentPane().setBackground(new Color(129, 195, 230));
		initGlobalFontSetting(GLOBAL_FONT);
		add(createRootContainer(tabbedPanels));
	}

	private Container createRootContainer(List<WillBeInMainTabbed> tabbedPanels) {
		List<String> allowedTabList = Arrays.asList(PropertiesUtil.getValueAsStringArray("ui.tabs"));
		JTabbedPane tabbedPane = new JTabbedPane();
		for (Iterator<WillBeInMainTabbed> iterator = tabbedPanels.iterator(); iterator.hasNext();) {
			WillBeInMainTabbed willBeInTabbed = iterator.next();
			if (!allowedTabList.contains(willBeInTabbed.getTabTitle())) {
				continue;
			}
			tabbedPane.addTab(willBeInTabbed.getTabTitle(), (Component) willBeInTabbed);
		}
		if (tabbedPane.getTabCount() == 0) {
			tabbedPane.add(tabbedPanels.get(0).getTabTitle(), (Component) tabbedPanels.get(0));
		}
		//1 - Trigger first tab panel change event
		tabbedPane.setSelectedIndex(-1);
		tabbedPane.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				WillBeInMainTabbed panel = (WillBeInMainTabbed) tabbedPane.getSelectedComponent();
				if (!panel.hasMainUI()) {
					panel.createMainUI();
				} else {
					if (panel instanceof ReportPanel) {
						panel.loadDatagrid();
					}
				}
			}
		});
		//2 - Trigger first tab panel change event
		tabbedPane.setSelectedIndex(0);
		return tabbedPane;
	}
	
	private WillBeInMainTabbed getDepositPanel() {
		WillBeInMainTabbed dp = new DepositPanel();
		return dp;
	}

	private WillBeInMainTabbed getConsumePanel() {
		WillBeInMainTabbed dp = new ConsumePanel();
		return dp;
	}
	
	private WillBeInMainTabbed getBorrowPanel() {
		WillBeInMainTabbed dp = new BorrowPanel();
		return dp;
	}
	
	private WillBeInMainTabbed getLendPanel() {
		WillBeInMainTabbed dp = new LendPanel();
		return dp;
	}
	
	private WillBeInMainTabbed getReadMePanel() {
		WillBeInMainTabbed dp = new ReadMePanel();
		return dp;
	}
	
	private WillBeInMainTabbed getReportPanel() {
		WillBeInMainTabbed dp = new ReportPanel();
		return dp;
	}

	private void initGlobalFontSetting(Font fnt) {
		FontUIResource fontRes = new FontUIResource(fnt);
		for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource)
				UIManager.put(key, fontRes);
		}
	}
		
	/**
	 * UI Entry
	 */
	public void start() {
		setVisible(true);
	}

	public static void main(String[] args) {
		new MainFrame().start();
	}
}
