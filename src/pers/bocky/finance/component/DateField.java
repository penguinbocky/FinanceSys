package pers.bocky.finance.component;

import java.awt.GridLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JPanel;
import javax.swing.JTextField;

import pers.bocky.finance.listener.DateDocument;

public class DateField extends JPanel {

	private static final long serialVersionUID = 1L;

	private JTextField yearField;
	private JTextField monthField;
	private JTextField dayField;
	
	public DateField() {
		super();
		initUI();
	}

	private void initUI() {
		setLayout(new GridLayout(1, 3));
		yearField = new JTextField(4);
		monthField = new JTextField(2);
		dayField = new JTextField(2);
		
		yearField.setToolTipText("年份（如：2018）");
		monthField.setToolTipText("月份（如：06）");
		dayField.setToolTipText("日期（如：12）");
		
		DateDocument dd1 = new DateDocument(DateDocument.FieldName.YEAR);
		DateDocument dd2 = new DateDocument(DateDocument.FieldName.MONTH);
		DateDocument dd3 = new DateDocument(DateDocument.FieldName.DAY);
		yearField.setDocument(dd1);
		monthField.setDocument(dd2);
		dayField.setDocument(dd3);
		
		yearField.setText("" + Calendar.getInstance().get(Calendar.YEAR));
		monthField.setText("" + (Calendar.getInstance().get(Calendar.MONTH) + 1));
		dayField.setText("" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		
		yearField.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusGained(FocusEvent e)
            {
            	yearField.setText("");
            }
            @Override
            public void focusLost(FocusEvent e)
            {
            	if (yearField.getText().equals("")) {
            		yearField.setText("" + Calendar.getInstance().get(Calendar.YEAR));
				}
            }
        });
		
		monthField.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusGained(FocusEvent e)
            {
            	monthField.setText("");
            }
            @Override
            public void focusLost(FocusEvent e)
            {
            	if (monthField.getText().equals("")) {
					monthField.setText("" + (Calendar.getInstance().get(Calendar.MONTH) + 1));
				}
            }
        });
		
		dayField.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusGained(FocusEvent e)
            {
            	dayField.setText("");
            }
            @Override
            public void focusLost(FocusEvent e)
            {
            	if (dayField.getText().equals("")) {
            		dayField.setText("" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
				}
            }
        });
		
		add(yearField);
		add(monthField);
		add(dayField);
	}

	/**
	 * yyyy-MM-dd
	 * @return
	 */
	public String getResultAsString() {
		return yearField.getText().trim()
				+ "-"
				+ monthField.getText().trim()
				+ "-" 
				+ dayField.getText().trim();
	}
	
	public Timestamp getResultAsTimestamp() {
		try {
			return new Timestamp(new SimpleDateFormat("yyyy-MM-dd").parse(getResultAsString()).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void fillFields(String year, String month, String day) {
		yearField.setText(year);
		monthField.setText(month);
		dayField.setText(day);
	}
}
