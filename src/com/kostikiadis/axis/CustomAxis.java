package com.kostikiadis.axis;

import java.util.ArrayList;

public class CustomAxis {

	private ArrayList<AxisLabel> labelsList = new ArrayList<>();

	private String title;

	private double minValue;
	private double maxValue;
	private double tickValue;
	private boolean isAutoScaling = true;

	// taken by JavaFX NumberAxis Source code
	private static final double[] TICK_UNIT_DEFAULTS = { 1.0E-10d, 2.5E-10d, 5.0E-10d, 1.0E-9d, 2.5E-9d, 5.0E-9d,
			1.0E-8d, 2.5E-8d, 5.0E-8d, 1.0E-7d, 2.5E-7d, 5.0E-7d, 1.0E-6d, 2.5E-6d, 5.0E-6d, 1.0E-5d, 2.5E-5d, 5.0E-5d,
			1.0E-4d, 2.5E-4d, 5.0E-4d, 0.0010d, 0.0025d, 0.0050d, 0.01d, 0.025d, 0.05d, 0.1d, 0.25d, 0.5d, 1.0d, 2.5d,
			5.0d, 10.0d, 25.0d, 50.0d, 100.0d, 250.0d, 500.0d, 1000.0d, 2500.0d, 5000.0d, 10000.0d, 25000.0d, 50000.0d,
			100000.0d, 250000.0d, 500000.0d, 1000000.0d, 2500000.0d, 5000000.0d, 1.0E7d, 2.5E7d, 5.0E7d, 1.0E8d, 2.5E8d,
			5.0E8d, 1.0E9d, 2.5E9d, 5.0E9d, 1.0E10d, 2.5E10d, 5.0E10d, 1.0E11d, 2.5E11d, 5.0E11d, 1.0E12d, 2.5E12d,
			5.0E12d };

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
		
		
		if (this.minValue > value) {
			this.minValue = value;
		} else if (this.maxValue < value) {
			maxValue = value;
		}

		for (int i = 0; i < TICK_UNIT_DEFAULTS.length; i++) {

			double tickUnitDefault = TICK_UNIT_DEFAULTS[i];

			if ((maxValue - minValue) / tickUnitDefault <= 15) {
				tickValue = tickUnitDefault;
				break;
			}
		}
		
		while (prevMin >= minValue) {
			prevMin = prevMin - tickValue;
		}
		if (minValue >= 0 && prevMin < 0) {
			prevMin = 0;
		}

		minValue = prevMin;

		while (prevMax <= maxValue) {
			prevMax = prevMax + tickValue;
		}
		maxValue = prevMax;

		labelsList.clear();
		initLabels();
		return 1;
	}

	public void setMaxValue(double max) {
		this.maxValue = Math.ceil(max);
	}

	public void setMinValue(double min) {
		this.minValue = Math.floor(min);
	}

}
