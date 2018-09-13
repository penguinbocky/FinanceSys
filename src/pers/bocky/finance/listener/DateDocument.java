package pers.bocky.finance.listener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class DateDocument extends PlainDocument {

	public static enum FieldName {
		YEAR, MONTH, DAY
	}

	private static final long serialVersionUID = 1L;
	private FieldName fieldName;

	public DateDocument(FieldName fieldName) {
		super();
		this.fieldName = fieldName;
	}

	@Override
	public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
		// Ensure input is(type one by one) / are(copy&paste all) number(s)
		Pattern pattern = Pattern.compile("[0-9]+");
		Matcher matcher = pattern.matcher(str);
		if (!matcher.matches()) {
			return;
		}

		int input = Integer.parseInt(getText(0, getLength()) + str);

		switch (fieldName) {
		case YEAR:
			if (getLength() + str.length() > 4) {
				return;
			} else {
				if (input <= 0 || input > 2100) {
					return;
				} else {
					super.insertString(offset, str, a);
				}
			}
			break;
		case MONTH:
			if (getLength() + str.length() > 2) {
				return;
			} else {
				if (input <= 0 || input > 12) {
					return;
				} else {
					super.insertString(offset, str, a);
				}
			}
			break;
		case DAY:
			// TODO switch max value by month
			if (getLength() + str.length() > 2) {
				return;
			} else {
				if (input <= 0 || input > 31) {
					return;
				} else {
					super.insertString(offset, str, a);
				}
			}
			break;
		default:
			break;
		}
		
	}

}
