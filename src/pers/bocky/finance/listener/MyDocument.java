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
	private JButton btn;
	private JTextField[] fields;
	  
	public MyDocument(int maxLength, JButton btn, JTextField... fields){ 
		super(); 
		this.maxLength = maxLength;
		this.btn = btn;
		this.fields = fields;
	} 
	public MyDocument(){ 
		this(10, null); 
	} 
	
	public MyDocument(boolean onlyAllowNumber, int maxLength) {
		super();
		this.onlyAllowNumber = onlyAllowNumber;
		this.maxLength = maxLength;
	}
	
	private boolean validAllFieldLength() {
		for (int i = 0; i < fields.length; i++) {
			JTextField jTextField = fields[i];
			if (jTextField.getText().equals("")) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public void insertString(int offset, String str, AttributeSet a)
			throws BadLocationException {
		if (onlyAllowNumber) {
			Pattern pattern = Pattern.compile("^[1-9][0-9]{0,5}\\.?[0-9]{0,2}|(0\\.[0-9]{1}[1-9]?)$");
			Matcher matcher = pattern.matcher(getText(0,  getLength()) + str);
			System.out.println(getText(0,  getLength()) + str);
			if (!matcher.matches()) {
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
		if (validAllFieldLength()) {
			btn.setEnabled(true);
		} else {
			btn.setEnabled(false);
		}
	}
	@Override
	public void removeUpdate(DocumentEvent e) {
		if (validAllFieldLength()) {
			btn.setEnabled(true);
		} else {
			btn.setEnabled(false);
		}
	}
	@Override
	public void changedUpdate(DocumentEvent e) {
		// TODO Auto-generated method stub
		
	}
}
