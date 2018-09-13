package pers.bocky.finance.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class DataGrid extends JTable {

	private static final long serialVersionUID = 1L;

	private DefaultTableModel datagridModel;
	
	public DataGrid(String[] colNames) {
		super();
		init(colNames);
	}

	@SuppressWarnings("serial")
	private void init(String[] colNames) {
		setRowHeight(20);
		setFont(new Font("", Font.PLAIN, 16));
		setBackground(new Color(199, 237, 204, 255));
//		setForeground(new Color(255, 60, 60, 255));
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					int row = DataGrid.this.getSelectedRow();
					System.out.println(row);
				}
			}
			
		});
		
		datagridModel = new DefaultTableModel(new String[][]{}, colNames){
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		setModel(datagridModel);
		
		TableColumnModel columnModel = getColumnModel();
		TableColumn tableCol = null;
		for (int col = 0; col < columnModel.getColumnCount(); col++) {
			tableCol = columnModel.getColumn(col);
			if (col == 0) {
				tableCol.setPreferredWidth(0);
				tableCol.setMinWidth(0);
				tableCol.setMaxWidth(0);
			} else if (col == 1) {
				tableCol.setPreferredWidth(0);
				tableCol.setMinWidth(0);
				tableCol.setMaxWidth(0);
			} else if (col == 2) {
				tableCol.setPreferredWidth(100);
				tableCol.setMinWidth(100);
//				tableCol.setMaxWidth(100);
			} else if (col == 3) {
				tableCol.setPreferredWidth(100);
				tableCol.setMinWidth(100);
//				tableCol.setMaxWidth(100);
			} else if (col == 4) {
				tableCol.setPreferredWidth(80);
				tableCol.setMinWidth(80);
//				tableCol.setMaxWidth(80);
			}
		}
	}
	
	@Override
	public String getToolTipText(MouseEvent e) {  
        int row = this.rowAtPoint(e.getPoint());  
        int col = this.columnAtPoint(e.getPoint());  
        String tiptextString = null;  
        if(row > -1 && col > -1){  
            Object value = this.getValueAt(row, col);  
            if(null != value && !"".equals(value))  
                tiptextString=value.toString();
        }  
        return tiptextString;  
    } 
	
	/**
	 * Clear all the current rows.
	 */
	public void clear() {
		datagridModel.setRowCount(0);
	}
	
	/**
	 * Add a single row after clearing all old rows.
	 * @param dataVector
	 */
	public void setData(Vector<String> dataVector) {
		clear();
		addData(dataVector);
	}
	
	/**
	 * Add new rows after clearing all old rows.
	 * @param dataVectorList
	 */
	public void setData(List<Vector<String>> dataVectorList) {
		clear();
		addData(dataVectorList);
	}
	
	/**
	 * Append a single row to the last row.
	 * @param dataVector
	 */
	public void addData(Vector<String> dataVector) {
		datagridModel.addRow(dataVector);
	}
	
	/**
	 * Append a data list to the last row.
	 * @param dataVectorList
	 */
	public void addData(List<Vector<String>> dataVectorList) {
		for (int i = 0; i < dataVectorList.size(); i++) {
			datagridModel.addRow(dataVectorList.get(i));
		}
	}
}
