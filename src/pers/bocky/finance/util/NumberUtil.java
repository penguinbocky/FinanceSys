package pers.bocky.finance.util;

import java.util.List;

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
}
