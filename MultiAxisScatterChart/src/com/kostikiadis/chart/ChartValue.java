package com.kostikiadis.chart;

public class ChartValue {
	private int axis;

	private double xValue;
	private double yValue;
	private double strokeSize;

	public ChartValue(int axis, double xValue, double yValue, double strokeSize) {
		super();
		this.axis = axis;
		this.xValue = xValue;
		this.yValue = yValue;
		this.strokeSize = strokeSize;
	}

	public int getAxis() {
		return axis;
	}

	public double getX() {
		return xValue;
	}

	public double getY() {
		return yValue;
	}

	public double getStrokeSize() {
		return strokeSize;
	}

}