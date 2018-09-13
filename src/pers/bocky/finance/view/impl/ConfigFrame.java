package pers.bocky.finance.view.impl;

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

import pers.bocky.finance.view.WillBeInConfigTabbed;

public class ConfigFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private final int WIDTH = 800;
	private final int HEIGHT = 640;
	
	static class ConfigFrameLazyHolder {
		public static ConfigFrame cf = new ConfigFrame();
	}
	
	public ConfigFrame() throws HeadlessException {
		super();
		
		setTitle("配置界面");
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((int)(d.getWidth() - WIDTH) / 2, (int)(d.getHeight() - HEIGHT) / 2, 
				WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);	
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				ConfigFrame.this.dispose();
			}
		});
		setResizable(true);
		
		List<WillBeInConfigTabbed> tabbedPanels = new ArrayList<WillBeInConfigTabbed>();
//		tabbedPanels.add(getCategoryPanel());
		tabbedPanels.add(getTypeConfigPanel());
		add(createRootContainer(tabbedPanels));
	}

	private Container createRootContainer(List<WillBeInConfigTabbed> tabbedPanels) {
		JTabbedPane tabbedPane = new JTabbedPane();
		for (Iterator<WillBeInConfigTabbed> iterator = tabbedPanels.iterator(); iterator.hasNext();) {
			WillBeInConfigTabbed willBeInTabbed = iterator.next();
			tabbedPane.addTab(willBeInTabbed.getTabTitle(), (Component) willBeInTabbed);
		}
		return tabbedPane;
	}
	
	private WillBeInConfigTabbed getCategoryPanel() {
		WillBeInConfigTabbed dp = new CategoryConfigPanel();
		return dp;
	}
	
	private WillBeInConfigTabbed getTypeConfigPanel() {
		WillBeInConfigTabbed dp = new TypeConfigPanel();
		return dp;
	}

	/**
	 * UI Entry
	 */
	public void start() {
		setVisible(true);
	}

}
