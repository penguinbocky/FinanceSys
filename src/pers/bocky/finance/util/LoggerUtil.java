package pers.bocky.finance.util;

import java.awt.Color;
import java.awt.Font;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import pers.bocky.finance.bean.LogBean;

public enum LoggerUtil {
	SQLogger("SQL Logger", "on".equals(PropertiesUtil.getValue("debug.switch")));
	
	private JFrame frame;
	private JTextArea output;
	private boolean debugMode;
	
	LoggerUtil(String type, boolean debugMode) {
		this.debugMode = debugMode;
		
		if (debugMode) {
			frame = new JFrame(type);
			frame.setSize(800, 400);
			
			output = new JTextArea();
			output.setLineWrap(true);
			output.setWrapStyleWord(true);
			output.setForeground(new Color(63, 169, 36, 255));
			output.setBackground(Color.BLACK);
			output.setEditable(false);
			output.setFont(new Font("Microsoft Yahei", Font.PLAIN, 10));
			
			JScrollPane scrollPane = new JScrollPane(output);
			frame.add(scrollPane);
		}
	}
	
	public void log(String log) {
		if (debugMode) {
			System.out.println(new Date().toString() + " > " + log); 
		}
	}
	
	public void log(LogBean log) {
		if (debugMode) {
			logToPanel(log);
		}
	}
	
	private void logToPanel(LogBean log) {
		if (!frame.isVisible()) {
			frame.setVisible(true);
		}
		output.append("[" + log.getDate() + "] > " + log.getInfo() + "\n");
	}
}
