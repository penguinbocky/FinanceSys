package pers.bocky.finance.component;

import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

public class DropDown<E, T> extends JComboBox<String> {

	private static final long serialVersionUID = 1L;

	private DefaultComboBoxModel<String> model;
	private List<E> list;
	
	public DropDown(List<E> list) {
		super();
		this.list = list;
		init();
	}
	
	private void init() {
		final String[] actions = new String[list.size()];
		int i = 0;
		for (Iterator<E> iterator = list.iterator(); iterator.hasNext();) {
			E typeBean = iterator.next();
//			actions[i++] = typeBean.getTypeId().toString();
		}
		
		setModel(new DefaultComboBoxModel<String>(actions));
	}
}
