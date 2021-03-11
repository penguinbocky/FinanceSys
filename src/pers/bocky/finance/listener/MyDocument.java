package pers.bocky.finance.listener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class MyDocument extends PlainDocument implements DocumentListener {

	private static final long serialVersionUID = 1L;
	private boolean onlyAllowNumber;
	private int maxLength;
	private JButton[] affectedBtns;
	private JTextField[] consideredFields;
	
	public MyDocument(int maxLength, JTextField[] fields, JButton[] btns){ 
		super(); 
		this.maxLength = maxLength;
		this.affectedBtns = btns;
		this.consideredFields = fields;
	}
	
	public MyDocument(int maxLength, JTextField field, JButton btn) {
		this(maxLength, new JTextField[]{ field }, new JButton[] { btn });
	}
	
	public MyDocument(boolean onlyAllowNumber, int maxLength, JTextField[] fields, JButton[] btns) {
		this(maxLength, fields, btns);
		this.onlyAllowNumber = onlyAllowNumber;
	}
	
	private boolean validFieldLength() {
		for (int i = 0; i < consideredFields.length; i++) {
			JTextField jTextField = consideredFields[i];
			if (jTextField.getText().equals("")) {
				return false;
			}
		}
		return true;
	}
	
	private boolean validText(String text) {
		if (onlyAllowNumber) {
			Pattern pattern = Pattern.compile("(\\-?)([1-9][0-9]*)(\\.[0-9]+)?");
			Matcher matcher;
			System.out.println("checking..." + text);
			matcher = pattern.matcher(text);
			if (!matcher.matches()) {
				System.out.println("valid whole Text > no matches");
				return false;
			}
		}
		System.out.println("valid whole Text > success");
		return true;
	}
	
	@Override
	public void insertString(int offset, String str, AttributeSet a)
			throws BadLocationException {
		System.out.println("insertString , onlyAllowNumber " + str + "," + onlyAllowNumber);
		if (onlyAllowNumber) {
			Pattern pattern = Pattern.compile("[\\-0-9\\.]*"); // the * is for directly insertion of a clicked multi-number text
			Matcher matcher = pattern.matcher(str);
			if (!matcher.matches()) {
				System.out.println("insertString>no matches");
				return;
			}
		}
		
		if (getLength() + str.length() > maxLength) {
			return;
		} else {
			super.insertString(offset, str, a);
		}
	}
	
	@Override
	public void insertUpdate(DocumentEvent e) {
		System.out.println("insertUpdate...");
		if (validFieldLength()) {
			Boolean enabled = true;
			try {
				if (!validText(e.getDocument().getText(0, e.getDocument().getLength()))) {
					enabled = false;
				} else {
					enabled = true;
				}
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			for (JButton btn : affectedBtns) {
				btn.setEnabled(enabled);
			}
		} else {
			for (JButton btn : affectedBtns) {
				btn.setEnabled(false);
			}
		}
	}
	@Override
	public void removeUpdate(DocumentEvent e) {
		if (validFieldLength()) {
			Boolean enabled = true;
			try {
				if (!validText(e.getDocument().getText(0, e.getDocument().getLength()))) {
					enabled = false;
				} else {
					enabled = true;
				}
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			for (JButton btn : affectedBtns) {
				btn.setEnabled(enabled);
			}
		} else {
			for (JButton btn : affectedBtns) {
				btn.setEnabled(false);
			}
		}
	}
	@Override
	public void changedUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		
	}
}
