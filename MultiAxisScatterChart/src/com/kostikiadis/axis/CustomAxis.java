package com.kostikiadis.axis;

import java.util.ArrayList;

public class CustomAxis {

	private ArrayList<AxisLabel> labelsList = new ArrayList<>();

	private String title;

	private double minValue;
	private double maxValue;
	private double tickValue;
	private boolean isAutoScaling = true;

	public CustomAxis() {
		this(0, 15, 1);
	}

	public CustomAxis(double minValue, double maxValue, double tickValue) {
		this.minValue = minValue;
		this.tickValue = tickValue;
		this.maxValue = maxValue;
		title = "Default Title";
		initLabels();
	}

	private void initLabels() {
		for (double i = minValue; i <= maxValue; i = i + tickValue) {
			if (i == (int) i) {
				labelsList.add(new AxisLabel(String.valueOf((int) i)));
			} else {
				labelsList.add(new AxisLabel(String.valueOf(round(i, 2))));
			}
		}
	}

	public ArrayList<AxisLabel> getLabelsList() {
		return labelsList;
	}

	public void setLabelsList(ArrayList<AxisLabel> labelsList) {
		this.labelsList = labelsList;
	}

	public double getMinValue() {
		return minValue;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public double getTickValue() {
		return tickValue;
	}

	public void setTickValue(double tickValue) {
		this.tickValue = tickValue;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ArrayList<String> getLabelsValues() {
		ArrayList<String> values = new ArrayList<>();

		for (int i = 0; i < labelsList.size(); i++) {
			values.add(String.valueOf(labelsList.get(i).getValue()));
		}

		return values;
	}

	public double[] getPoints() {
		double points[] = new double[labelsList.size()];

		for (int i = 0; i < labelsList.size(); i++) {
			points[i] = Double.parseDouble(labelsList.get(i).getValue());
		}

		return points;
	}

	public void setAutoScaling(boolean mode) {
		isAutoScaling = mode;
	}

	public boolean isAutoScaling() {
		return isAutoScaling;
	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	public int checkScaling(double value) {

		double prevMin = this.minValue;
		double prevMax = this.maxValue;
		
		if (value >= minValue && value <= maxValue) {
			return 0;
		}

		if (this.minValue > value) {
			this.minValue = value;
		} else {
			maxValue = value;
		}

		double prefTickValue = 0.01;

		if (isAutoScaling) {
			prefTickValue = 0.05;

			while ((maxValue - minValue) / prefTickValue > 20) {
				prefTickValue = round(prefTickValue * 10, 2);
			}
			tickValue = prefTickValue;
		}
		
		if(prevMin != minValue){
			while(prevMin > minValue){
				prevMin = prevMin - tickValue;
			}
			minValue = prevMin;
		}
		
		if(prevMax != maxValue){
			while(prevMax < maxValue){
				prevMax = prevMax + tickValue;
			}
			maxValue = prevMax;
		}

		labelsList.clear();
		initLabels();
		return 1;
	}

}
