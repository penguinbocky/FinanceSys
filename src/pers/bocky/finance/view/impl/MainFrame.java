package pers.bocky.finance.view.impl;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pers.bocky.finance.view.WillBeInMainTabbed;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private final int WIDTH = 860;
	private final int HEIGHT = 640;
	
	public MainFrame() throws HeadlessException {
		super();
		
		setTitle("财务管理系统");
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
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
		
		getContentPane().setBackground(new Color(129, 195, 230));
		add(createRootContainer(tabbedPanels));
	}

	private Container createRootContainer(List<WillBeInMainTabbed> tabbedPanels) {
		JTabbedPane tabbedPane = new JTabbedPane();
		for (Iterator<WillBeInMainTabbed> iterator = tabbedPanels.iterator(); iterator.hasNext();) {
			WillBeInMainTabbed willBeInTabbed = iterator.next();
			tabbedPane.addTab(willBeInTabbed.getTabTitle(), (Component) willBeInTabbed);
		}
		//1 - Trigger first tab panel change event
		tabbedPane.setSelectedIndex(-1);
		tabbedPane.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				WillBeInMainTabbed panel = (WillBeInMainTabbed) tabbedPane.getSelectedComponent();
				if (!panel.hasMainUI()) {
					panel.createMainUI();
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
