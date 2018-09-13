package pers.bocky.finance.view.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class LoginFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	private final int WIDTH = 360;
	private final int HEIGHT = 200;
	
	public LoginFrame() throws HeadlessException {
		super();
		
		setTitle("财务管理系统 - 选择登陆界面");
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
		setResizable(false);
		getContentPane().setBackground(new Color(129, 195, 230));
		setLayout(null);//To make free layout
		add(createUI());
		start();
	}

	private JPanel createUI() {
		JPanel p = new JPanel(new BorderLayout());
		p.setOpaque(false);
		p.setBounds(WIDTH / 5, HEIGHT / 3, 200, 40);
		JButton btn1 = new JButton("主界面");
		JButton btn2 = new JButton("配置界面");
		btn1.addActionListener(this);
		btn2.addActionListener(this);
		
		p.add(btn1, BorderLayout.WEST);
		p.add(btn2, BorderLayout.EAST);
		
		return p;
	}

	private void stop() {
		setVisible(false);
	}
	
	private void start() {
		setVisible(true);;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "主界面":
			LoginFrame.this.stop();
			new MainFrame().start();
			break;
		case "配置界面":
			LoginFrame.this.stop();
			new ConfigFrame().start();
			break;
		default:
			break;
		}
	}

//	public static void main(String[] args) {
//		new LoginFrame().start();
//	}
}
