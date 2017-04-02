package com.kostikiadis.chart;

import static thorwin.math.Math.polyfit;
import static thorwin.math.Math.polynomial;

import java.util.ArrayList;

import com.kostikiadis.axis.CustomAxis;

import javafx.scene.Node;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;

public class MultiAxisScatterChart extends Region {

	public static final int FIRST_Y_AXIS = 0;
	public static final int SECOND_Y_AXIS = 1;

	private static final int LINEAR = 1;
	private static final int POLYNOMIAL = 2;
	private static final double VALUE_POINT_SIZE = 5;

	private double WIDTH;
	private double HEIGHT;

	private Line xAxisLine;
	private Line yLeftAxisLine;
	private Line yRightAxisLine;

	private Rectangle rectBorder;

	private double borderInsets = 60;

	private ArrayList<ChartValue> userValues = new ArrayList<>();
	private ArrayList<ChartValue> y1RegressionValues = new ArrayList<>();
	private ArrayList<ChartValue> y2RegressionValues = new ArrayList<>();

	private CustomAxis xAxis;
	private CustomAxis y1Axis;
	private CustomAxis y2Axis;

	private String chartTitle = "Chart Title";

	private int Y1_TRENDLINE_TYPE = -1;
	private int Y2_TRENDLINE_TYPE = -1;
	private Text y1TitleText;
	private Text y2TitleText;
	private Text xtitleText;
	private Text chartTitleText;

	private ArrayList<Node> axisComponts = new ArrayList<>();
	private ArrayList<Node> valueComponents = new ArrayList<>();

	private boolean isAutoUpdated = true;

	private Color strokeColor = Color.BLUE;
	private Color strokeColor2 = Color.RED;

	public MultiAxisScatterChart() {
		this(500, 500);

	}

	public MultiAxisScatterChart(int width, int height) {
		this(width, height, new CustomAxis(), new CustomAxis(), new CustomAxis());
	}

	public MultiAxisScatterChart(int width, int height, CustomAxis xAxis, CustomAxis y1Axis, CustomAxis y2Axis) {

		WIDTH = width;
		HEIGHT = height;

		this.setStyle("-fx-background-color:white;");

		this.xAxis = xAxis;
		this.y1Axis = y1Axis;
		this.y2Axis = y2Axis;

		initChart();

	}

	private void initChart() {
		setBorders();
		setTitles();
		setAxis();

		updateValues();
	}

	private void setTitles() {

		if (y1TitleText != null) {
			getChildren().removeAll(y1TitleText, y2TitleText, xtitleText, chartTitleText);
		}

		Font titleFont = new Font(16);

		y1TitleText = new Text(y1Axis.getTitle());
		y1TitleText.setFont(titleFont);
		y1TitleText.relocate(borderInsets - getLabelHeight(y1TitleText) / 2,
				HEIGHT / 2 + getLabelWidth(y1TitleText) / 2);
		y1TitleText.getTransforms().add(new Rotate(-90));

		y2TitleText = new Text(y2Axis.getTitle());
		y2TitleText.setFont(titleFont);
		y2TitleText.relocate(WIDTH - borderInsets + getLabelHeight(y2TitleText) / 2,
				HEIGHT / 2 - getLabelWidth(y2TitleText) / 2);
		y2TitleText.getTransforms().add(new Rotate(90));

		xtitleText = new Text(xAxis.getTitle());
		xtitleText.setFont(titleFont);
		xtitleText.relocate(WIDTH / 2 - getLabelWidth(xtitleText) / 2,
				HEIGHT - borderInsets / 2 - getLabelHeight(xtitleText) / 2);

		chartTitleText = new Text(chartTitle);
		chartTitleText.setFont(titleFont);
		chartTitleText.relocate(WIDTH / 2 - getLabelWidth(chartTitleText) / 2, 10 + getLabelHeight(chartTitleText) / 2);

		getChildren().addAll(y1TitleText, y2TitleText, xtitleText, chartTitleText);
	}

	private double getLabelHeight(Text text) {
		text.applyCss();
		return text.getLayoutBounds().getHeight();
	}

	private double getLabelWidth(Text text) {
		text.applyCss();
		return text.getLayoutBounds().getWidth();
	}

	private void setBorders() {
		Color rectBorderFillColor = Color.TRANSPARENT;
		Color rectBorderStrokeColor = Color.BLACK;

		rectBorder = new Rectangle(10, 10, WIDTH - 20, HEIGHT - 20);
		rectBorder.setFill(rectBorderFillColor);
		rectBorder.setStroke(rectBorderStrokeColor);

		getChildren().add(rectBorder);

	}

	private void setAxis() {

		getChildren().removeAll(axisComponts);
		axisComponts.clear();

		setXAxisLine();
		setLeftYAxis();
		setRightYAxis();

	}

	private void setXAxisLine() {

		ArrayList<String> axisValues = xAxis.getLabelsValues();

		double x1 = borderInsets * 2;
		double x2 = WIDTH - borderInsets * 2;

		double y = HEIGHT - borderInsets * 1.2;

		xAxisLine = new Line(x1, y, x2, y);
		axisComponts.add(xAxisLine);

		getChildren().add(xAxisLine);

		double xAxisLineWidth = x2 - x1;

		double labelSpacing = xAxisLineWidth / (axisValues.size() - 1);

		for (int i = 0; i < axisValues.size(); i++) {
			addXTickLine(axisValues.get(i), x1 + i * labelSpacing, y);
		}

	}

	private void setLeftYAxis() {

		ArrayList<String> axisValues = y1Axis.getLabelsValues();

		double x = borderInsets * 2;

		double y1 = HEIGHT - borderInsets * 1.2;
		double y2 = borderInsets;

		yLeftAxisLine = new Line(x, y1, x, y2);
		yLeftAxisLine.setStroke(strokeColor);

		axisComponts.add(yLeftAxisLine);

		getChildren().add(yLeftAxisLine);

		double xAxisLineWidth = y2 - y1;
		double labelSpacing = xAxisLineWidth / (axisValues.size() - 1);

		for (int i = 0; i < axisValues.size(); i++) {
			addYTickLine(axisValues.get(i), x, y1 + i * labelSpacing, 0);

			Line horizontalLine = new Line(x, y1 + i * labelSpacing, WIDTH - borderInsets * 2, y1 + i * labelSpacing);
			axisComponts.add(horizontalLine);

			horizontalLine.setStrokeWidth(0.1);
			getChildren().add(horizontalLine);

		}
	}

	private void setRightYAxis() {

		ArrayList<String> axisValues = y2Axis.getLabelsValues();

		double x = WIDTH - borderInsets * 2;

		double y1 = HEIGHT - borderInsets * 1.2;
		double y2 = borderInsets;

		yRightAxisLine = new Line(x, y1, x, y2);
		yRightAxisLine.setStroke(strokeColor2);

		axisComponts.add(yRightAxisLine);

		getChildren().add(yRightAxisLine);

		double xAxisLineWidth = y2 - y1;

		double labelSpacing = xAxisLineWidth / (axisValues.size() - 1);

		for (int i = 0; i < axisValues.size(); i++) {
			addYTickLine(axisValues.get(i), x, y1 + i * labelSpacing, 1);
		}

	}

	private void addXTickLine(String value, double x, double y) {

		Line line = new Line(x, y - 5, x, y + 5);
		axisComponts.add(line);

		Text text = new Text(value);
		axisComponts.add(text);

		text.setX(x - 5);
		text.setY(y + 25);

		getChildren().addAll(line, text);

	}

	private void addYTickLine(String value, double x, double y, int index) {
		Line line = new Line(x - 5, y, x + 5, y);
		axisComponts.add(line);

		Text text = new Text(value);
		axisComponts.add(text);

		if (index == 0) {
			text.setX(x - 40);
		} else {
			text.setX(x + 15);
		}
		text.setY(y + 5);

		getChildren().addAll(line, text);

	}

	public void updateValues() {

		getChildren().removeAll(valueComponents);

		valueComponents.clear();

		for (ChartValue value : userValues) {
			drawPoint(value);
		}

		for (ChartValue value : y1RegressionValues) {
			drawPoint(value);
		}

		for (ChartValue value : y2RegressionValues) {
			drawPoint(value);
		}
	}

	private void drawPoint(ChartValue value) {

		Color fillColor = Color.TRANSPARENT;

		Circle circle = new Circle();

		CustomAxis yAxis;

		double x = value.getX();
		double y = value.getY();

		int axisNum = value.getAxis();

		if (axisNum == 0) {
			yAxis = y1Axis;
			circle.setStroke(strokeColor);
		} else {
			yAxis = y2Axis;
			circle.setStroke(strokeColor2);
		}

		double x_pos = getPosInXAxis(x);
		double y_pos = getPositionInYAxis(yAxis, yLeftAxisLine, y);

		if (isValueInBounds(x_pos, y_pos)) {
			valueComponents.add(circle);
			circle.setFill(fillColor);
			circle.setRadius(value.getStrokeSize());

			circle.setCenterX(x_pos);
			circle.setCenterY(y_pos);

			getChildren().add(circle);
		}

	}

	private boolean isValueInBounds(double x_pos, double y_pos) {
		return x_pos >= xAxisLine.getStartX() && x_pos <= xAxisLine.getEndX() && y_pos <= yLeftAxisLine.getStartY()
				&& y_pos >= yLeftAxisLine.getEndY();
	}

	private double getPosInXAxis(double value) {

		double xAxisMinValue = xAxis.getMinValue();
		double xAxisMaxValue = xAxis.getMaxValue();

		double maxValue = xAxisMaxValue - xAxisMinValue;
		value = value - xAxisMinValue;

		double percentage = value / maxValue;
		double width = xAxisLine.getEndX() - xAxisLine.getStartX();

		return xAxisLine.getStartX() + width * percentage;
	}

	private double getPositionInYAxis(CustomAxis axis, Line axisLine, double value) {

		double yAxisMin = axis.getMinValue();
		double yAxisMax = axis.getMaxValue();

		double maxValue = yAxisMax - yAxisMin;
		value = value - yAxisMin;

		double percentage = value / maxValue;
		double width = axisLine.getEndY() - axisLine.getStartY();

		return axisLine.getStartY() + width * percentage;

	}

	public void addValue(double x, double y, int axisIndex) {

		userValues.add(new ChartValue(axisIndex, x, y, VALUE_POINT_SIZE));
		changeAxisScaling(x, y, axisIndex);

		calculateTrendLines();

		if (isAutoUpdated) {
			updateValues();
		}
	}

	private void changeAxisScaling(double x, double y, int axisIndex) {

		int changes = 0;

		if (xAxis.isAutoScaling()) {
			if (!userValues.isEmpty()) {

				double max = getMaxXValue();
				double min = getMinXValue();

				if (min == max) {
					max = xAxis.getMaxValue() > max ? xAxis.getMaxValue() : max;
					min = xAxis.getMinValue() < min ? xAxis.getMinValue() : min;
				}

				xAxis.setMaxValue(max);
				xAxis.setMinValue(min);
			}

			changes += xAxis.checkScaling(x);
		}

		CustomAxis yAxis;

		if (axisIndex == FIRST_Y_AXIS) {
			yAxis = y1Axis;
		} else {
			yAxis = y2Axis;
		}
		if (yAxis.isAutoScaling()) {
			if (!userValues.isEmpty()) {

				double max = getMaxYValue(axisIndex);
				double min = getMinYValue(axisIndex);

				if (min == max) {
					max = yAxis.getMaxValue() > max ? yAxis.getMaxValue() : max;
					min = yAxis.getMinValue() < min ? yAxis.getMinValue() : min;
				}
				yAxis.setMaxValue(max);
				yAxis.setMinValue(min);
			}

			changes += yAxis.checkScaling(y);
		}

		if (changes > 0) {
			setAxis();
		}

	}

	private void calculateTrendLines() {
		if (Y1_TRENDLINE_TYPE != -1) {
			generateTrendLine(Y1_TRENDLINE_TYPE, FIRST_Y_AXIS);
		}
		if (Y2_TRENDLINE_TYPE != -1) {
			generateTrendLine(Y2_TRENDLINE_TYPE, SECOND_Y_AXIS);
		}
	}

	private void generateTrendLine(int mode, int axisIndex) {

		if (userValues == null || userValues.isEmpty() || userValues.size() < 4)
			return;

		if (axisIndex == FIRST_Y_AXIS) {
			y1RegressionValues.clear();
		} else {
			y2RegressionValues.clear();
		}

		try {
			double xPoins[] = getXPoints(userValues.size(), axisIndex);
			double yPoins[] = getYPoints(userValues.size(), axisIndex);
			double[] coefficients = polyfit(xPoins, yPoins, mode);

			double step = xAxis.getTickValue() / 200;

			for (double x = getMinXValue(userValues, axisIndex); x <= getMaxXValue(userValues, axisIndex); x += step) {
				double y = polynomial(x, coefficients);

				addTrendLineValue(x, y, axisIndex, 0.5);
			}
		} catch (Exception e) {

		}

	}

	private void addTrendLineValue(double x, double y, int axisNum, double size) {
		if (axisNum == FIRST_Y_AXIS) {
			y1RegressionValues.add(new ChartValue(axisNum, x, y, size));
		} else {
			y2RegressionValues.add(new ChartValue(axisNum, x, y, size));
		}
	}

	public void showPolynomialTrendLineOnAxis(int axisIndex) {
		if (axisIndex == FIRST_Y_AXIS) {
			Y1_TRENDLINE_TYPE = POLYNOMIAL;
		} else {
			Y2_TRENDLINE_TYPE = POLYNOMIAL;
		}
		generateTrendLine(POLYNOMIAL, axisIndex);
		updateValues();
	}

	public void showLinearTrendLineOnAxis(int axisIndex) {
		if (axisIndex == FIRST_Y_AXIS) {
			Y1_TRENDLINE_TYPE = LINEAR;
		} else {
			Y2_TRENDLINE_TYPE = LINEAR;
		}
		generateTrendLine(LINEAR, axisIndex);
		updateValues();

	}

	public void hidePolynomialTrendLineOnAxis(int axisIndex) {
		if (axisIndex == FIRST_Y_AXIS) {
			Y1_TRENDLINE_TYPE = -1;
		} else {
			Y2_TRENDLINE_TYPE = 1;
		}

	}

	public void hdieLinearTrendLineOnAxis(int axisIndex) {
		if (axisIndex == FIRST_Y_AXIS) {
			Y1_TRENDLINE_TYPE = -1;
		} else {
			Y2_TRENDLINE_TYPE = -1;
		}

	}

	private double getMinXValue(ArrayList<ChartValue> values, int axisIndex) {
		double min = Double.MAX_VALUE;
		for (ChartValue value : values) {
			if (value.getAxis() == axisIndex && value.getX() < min) {
				min = value.getX();
			}
		}
		return min;
	}

	private double getMaxXValue(ArrayList<ChartValue> values, int axisIndex) {

		double max = Double.MIN_VALUE;
		for (ChartValue value : values) {
			if (value.getAxis() == axisIndex && value.getX() > max) {
				max = value.getX();
			}
		}
		return max;
	}

	private double[] getYPoints(int size, int axisIndex) {
		ArrayList<Double> points = new ArrayList<>();

		for (ChartValue point : userValues) {
			if (point.getAxis() == axisIndex) {
				points.add(point.getY());
			}

		}

		double yPoints[] = new double[points.size()];

		for (int i = 0; i < points.size(); i++) {
			yPoints[i] = points.get(i);

		}

		return yPoints;
	}

	private double[] getXPoints(int size, int axisIndex) {
		ArrayList<Double> points = new ArrayList<>();

		for (ChartValue point : userValues) {
			if (point.getAxis() == axisIndex) {
				points.add(point.getX());
			}

		}

		double xPoints[] = new double[points.size()];

		for (int i = 0; i < points.size(); i++) {
			xPoints[i] = points.get(i);

		}

		return xPoints;
	}

	public void setTitle(String title) {
		this.chartTitle = title;
		setTitles();
	}

	public void clearData() {
		userValues.clear();
		y1RegressionValues.clear();
		y2RegressionValues.clear();
	}

	public void setAutoUpdate(boolean isAutoUpdated) {
		this.isAutoUpdated = isAutoUpdated;
	}

	private double getMinYValue(int axisIndex) {
		double min = Double.MAX_VALUE;
		for (ChartValue value : userValues) {
			if (value.getAxis() == axisIndex && min > value.getY()) {
				min = value.getY();
			}
		}
		return min;
	}

	private double getMaxYValue(int axisIndex) {
		double max = Double.MIN_VALUE;
		for (ChartValue value : userValues) {
			if (value.getAxis() == axisIndex && max < value.getY()) {
				max = value.getY();
			}
		}
		return max;
	}

	private double getMinXValue() {
		double min = Double.MAX_VALUE;
		for (ChartValue value : userValues) {
			if (min > value.getX()) {
				min = value.getX();
			}
		}
		return min;
	}

	private double getMaxXValue() {
		double max = Double.MIN_VALUE;
		for (ChartValue value : userValues) {
			if (max < value.getX()) {
				max = value.getX();
			}
		}

		return max;
	}

	public void setYAxisColor(int axisIndex, Color color) {
		if (axisIndex == FIRST_Y_AXIS) {
			strokeColor = color;
		} else {
			strokeColor2 = color;
		}
	}

}
