import com.kostikiadis.axis.CustomAxis;
import com.kostikiadis.chart.MultiAxisScatterChart;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Test extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		CustomAxis xAxis = new CustomAxis(80, 180, 20);
		xAxis.setTitle("Load (kg)");

		CustomAxis y1Axis = new CustomAxis(0, 3000, 500);
		y1Axis.setTitle("Force (N)");

		CustomAxis y2Axis = new CustomAxis(2150, 2500, 50);
		y2Axis.setTitle("Power (watt)");

		MultiAxisScatterChart chart = new MultiAxisScatterChart(850, 500, xAxis, y1Axis, y2Axis);
		chart.setTitle("Force, Power/Load");

		chart.addValue(100, 1000, MultiAxisScatterChart.FIRST_Y_AXIS);
		chart.addValue(100, 2298, MultiAxisScatterChart.FIRST_Y_AXIS);
		chart.addValue(110, 2193, MultiAxisScatterChart.FIRST_Y_AXIS);
		chart.addValue(120, 2469, MultiAxisScatterChart.FIRST_Y_AXIS);
		chart.addValue(130, 2332, MultiAxisScatterChart.FIRST_Y_AXIS);
		chart.addValue(140, 2404, MultiAxisScatterChart.FIRST_Y_AXIS);
		chart.addValue(150, 2399, MultiAxisScatterChart.FIRST_Y_AXIS);
		chart.addValue(160, 2240, MultiAxisScatterChart.FIRST_Y_AXIS);

		chart.showLinearTrendLineOnAxis(MultiAxisScatterChart.FIRST_Y_AXIS);

		chart.addValue(100, 2298, MultiAxisScatterChart.SECOND_Y_AXIS);
		chart.addValue(110, 2193, MultiAxisScatterChart.SECOND_Y_AXIS);
		chart.addValue(120, 2469, MultiAxisScatterChart.SECOND_Y_AXIS);
		chart.addValue(130, 2332, MultiAxisScatterChart.SECOND_Y_AXIS);
		chart.addValue(140, 2404, MultiAxisScatterChart.SECOND_Y_AXIS);
		chart.addValue(150, 2399, MultiAxisScatterChart.SECOND_Y_AXIS);
		chart.addValue(160, 2240, MultiAxisScatterChart.SECOND_Y_AXIS);

		chart.showPolynomialTrendLineOnAxis(MultiAxisScatterChart.SECOND_Y_AXIS);

		FlowPane valuePane = new FlowPane(15, 10);

		TextField xField = new TextField();
		TextField yField = new TextField();

		Button addButton = new Button("Add value");
		addButton.setOnAction(e -> {
			chart.addValue(Double.parseDouble(xField.getText()), Double.parseDouble(yField.getText()),
					MultiAxisScatterChart.FIRST_Y_AXIS);
		});

		valuePane.getChildren().addAll(xField, yField, addButton);

		BorderPane mainPane = new BorderPane();
		mainPane.setCenter(valuePane);
		mainPane.setBottom(chart);

		primaryStage.setScene(new Scene(mainPane));
		primaryStage.show();

	}

}
