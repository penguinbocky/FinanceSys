package pers.bocky.finance.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.function.BiPredicate;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class DataGrid extends JTable {

	private static final long serialVersionUID = 1L;

	private final static int BASE_ROW_HEIGHT = 24;
	private final static int BASE_FONT_SIZE = 14;
	private final static int ROW_HEIGHT;
	private final static Font GLOBAL_FONT;
	private final static double ratioForUI;
	
	static {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		ratioForUI = d.getHeight() / 768;
		ROW_HEIGHT = (int) (BASE_ROW_HEIGHT * ratioForUI);
		GLOBAL_FONT = new Font("Microsoft Yahei", Font.PLAIN, (int) (BASE_FONT_SIZE * d.getHeight() / 640));
	}
	
	private DefaultTableModel datagridModel;
	
	public DataGrid(String[] colNames, String[] hiddenColNames, String[] smallColNames, String[] largeColNames) {
		super();
		init(colNames, hiddenColNames, smallColNames, largeColNames);
	}

	private void init(String[] colNames, String[] hiddenColNames, String[] smallColNames, String[] largeColNames) {
		getTableHeader().setForeground(new Color(240, 240, 240));
		getTableHeader().setBackground(new Color(0, 0, 0, 240));
		
		setSelectionBackground(new Color(129, 194, 241));
		setSelectionForeground(Color.BLACK);
		
		setRowHeight(ROW_HEIGHT);
		setFont(GLOBAL_FONT);
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
		
		setDataGridDataModel(colNames);
		serPreferredColumnWidth(hiddenColNames, smallColNames, largeColNames);
	}

	private void serPreferredColumnWidth(String[] hiddenColNames, String[] smallColNames, String[] largeColNames) {
		if (hiddenColNames == null) {
			hiddenColNames = new String[] {};
		}
		if (smallColNames == null) {
			smallColNames = new String[] {};
		}
		if (largeColNames == null) {
			largeColNames = new String[] {};
		}
		
		TableColumnModel columnModel = getColumnModel();
		TableColumn tableCol = null;
		List<String> hiddenList = Arrays.asList(hiddenColNames);
		List<String> smallList = Arrays.asList(smallColNames);
		List<String> largeList = Arrays.asList(largeColNames);
		for (int col = 0; col < columnModel.getColumnCount(); col++) {
			tableCol = columnModel.getColumn(col);
			if (hiddenList.contains(tableCol.getHeaderValue())) {
				tableCol.setPreferredWidth(0);
				tableCol.setMinWidth(0);
				tableCol.setMaxWidth(0);
			} else if (smallList.contains(tableCol.getHeaderValue())) {
				tableCol.setPreferredWidth((int) (100 * ratioForUI));
				tableCol.setMinWidth((int) (100 * ratioForUI));
				tableCol.setMaxWidth((int) (120 * ratioForUI));
			} else if (largeList.contains(tableCol.getHeaderValue())) {
				tableCol.setPreferredWidth((int) (280 * ratioForUI));
				tableCol.setMinWidth((int) (240 * ratioForUI));
				tableCol.setMaxWidth((int) (360 * ratioForUI));
			}
		}
	}

	private void setDataGridDataModel(String[] colNames) {
		datagridModel = new DefaultTableModel(new String[][]{}, colNames){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		setModel(datagridModel);
	}
	
	public void setRowColor(int columnIndex1, int columnIndex2, BiPredicate<String, String> predict) {
		setRowColor(columnIndex1, columnIndex2, predict, null, null);
	}
	
	public void setRowColor(int columnIndex1, int columnIndex2, BiPredicate<String, String> predict, Color colorForFlag, Color colorForElse) {
		if (columnIndex1 >= getColumnCount() || columnIndex1 < 0 || columnIndex2 >= getColumnCount() || columnIndex2 < 0) {
			return;//throw exception
		}
		
//		Color old = getForeground();
		final Color colorForYes = colorForFlag == null ? new Color(34, 151, 18) : colorForFlag;
		final Color colorForNo = colorForElse == null ? new Color(232, 47, 58, 186) : colorForElse;
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				if (predict.test("" + getValueAt(row, columnIndex1), "" + getValueAt(row, columnIndex2))) {
					setForeground(colorForYes);
				} else {
					setForeground(colorForNo);
				}
				return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			}
			
		};
		
		int columnCount = getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            getColumn(getColumnName(i)).setCellRenderer(renderer);
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
