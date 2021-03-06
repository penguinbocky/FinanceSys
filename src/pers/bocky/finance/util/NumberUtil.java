package pers.bocky.finance.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import pers.bocky.finance.component.DataGrid;

public class NumberUtil {

	/**
	 * Sum all double values from a list, and keep two decimals.
	 * @param doubleList
	 * @return
	 */
	public static double sum(List<Double> doubleList) {
		double sum = 0;
		int size = doubleList.size();
		for (int i = 0; i < size; i++) {
			sum += doubleList.get(i);
		}
		
		return (double) Math.round(sum * 100) / 100;//保留两位小数
	}
	
	public static double sumAmount(DataGrid datagrid) {
		IntStream is = null;
		int[] selectedRowIndices = datagrid.getSelectedRows();
		if (null != selectedRowIndices && selectedRowIndices.length > 0) {
			is = IntStream.of(selectedRowIndices);
		} else {
			if (datagrid == null || datagrid.getModel().getRowCount() < 1) {
				return 0;
			}
			is = IntStream.range(0, datagrid.getModel().getRowCount());// Exclusive
		}
		
		List<Double> doubleList = new ArrayList<Double>();
		is.forEach((ele) -> {
			String amountStr = (String) datagrid.getValueAt(ele, 4);
			double amount = Double.parseDouble(amountStr.replaceAll(",", ""));
			doubleList.add(amount);
		});
		
		return doubleList.stream().reduce((acc, ele) -> acc += ele).orElse(0d);
	}
}
