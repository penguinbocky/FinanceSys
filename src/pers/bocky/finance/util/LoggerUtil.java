package pers.bocky.finance.util;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import pers.bocky.finance.bean.LogBean;

public enum LoggerUtil {
	SQLogger("SQL Logger", false);
	
	private JFrame frame;
	private JTextArea output;
	private boolean debugMode;
	
	LoggerUtil(String type, boolean debugMode) {
		this.debugMode = debugMode;
		
		if (debugMode) {
			frame = new JFrame(type);
			frame.setSize(800, 200);
			
			output = new JTextArea();
			output.setLineWrap(true);
			output.setWrapStyleWord(true);
//			output.setForeground(Color.GREEN);
//			output.setBackground(Color.BLACK);
			output.setEditable(false);
//			output.setFont(new Font("华文中宋", Font.PLAIN, 13));
			
			JScrollPane scrollPane = new JScrollPane(output);
			frame.add(scrollPane);
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
		output.append("[" + log.getDate().toGMTString() + "] >>> " + log.getInfo() + "\n");
	}
}
