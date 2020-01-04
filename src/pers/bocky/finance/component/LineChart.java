package pers.bocky.finance.component;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Map;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RefineryUtilities;

public class LineChart extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LineChart(String applicationTitle, String chartTitle, String category, Map<String, Double> dataMap) {
		super(applicationTitle);

		StandardChartTheme mChartTheme = new StandardChartTheme("CN");
		mChartTheme.setLargeFont(new Font("Microsoft Yahei", Font.PLAIN, 16));
		mChartTheme.setExtraLargeFont(new Font("Microsoft Yahei", Font.PLAIN, 26));
		mChartTheme.setRegularFont(new Font("Microsoft Yahei", Font.PLAIN, 12));
		ChartFactory.setChartTheme(mChartTheme);

		JFreeChart lineChart = ChartFactory.createLineChart(chartTitle, "时间(年/月)", "数量(元)",
		createCategoryDataset(category, dataMap), PlotOrientation.VERTICAL, true, true, false);
		lineChart.setBackgroundPaint(new Color(208, 223, 239));
		
		CategoryPlot plot = (CategoryPlot) lineChart.getPlot();
		plot.getDomainAxis().setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);
		plot.setNoDataMessage("没有可显示的数据！！！");
		plot.setForegroundAlpha(1f);
		plot.setBackgroundAlpha(0.4F);
		plot.setBackgroundPaint(new Color(204, 170, 126, 255));
		plot.setOutlinePaint(Color.ORANGE);
		plot.setOutlineStroke(new BasicStroke(1.f));
		plot.setOutlineVisible(true);
		plot.setForegroundAlpha(1.f);
		plot.setDomainGridlinePaint(Color.BLACK);
		plot.setDomainGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.BLACK);
		plot.setRangeGridlinesVisible(true);

		LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
		renderer.setSeriesFillPaint(0, Color.DARK_GRAY);
		renderer.setStroke(new BasicStroke(2.f));
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
		renderer.setBaseItemLabelFont(new Font("Dialog", Font.ROMAN_BASELINE, 10));
		renderer.setBaseItemLabelsVisible(true);
		
		lineChart.removeLegend();
		LegendTitle legend = lineChart.getLegend();
		if (legend != null) {
			legend.setItemFont(new Font("Microsoft Yahei", Font.BOLD, 16));
			legend.setItemPaint(Color.RED);
			legend.setBackgroundPaint(Color.LIGHT_GRAY);
		}

		ChartPanel chartPanel = new ChartPanel(lineChart);
		chartPanel.setPreferredSize(new Dimension(960, 580));
		setContentPane(chartPanel);
	}

	private CategoryDataset createCategoryDataset(String category, Map<String, Double> dataMap) {
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		if (null != dataMap) {
			for (Map.Entry<String, Double> entry : dataMap.entrySet()) {
				dataset.addValue(entry.getValue(), category, entry.getKey());
			}
		}

		return dataset;
	}

	public void showChart() {
		this.pack();
		RefineryUtilities.centerFrameOnScreen(this);
		this.setVisible(true);
	}

}