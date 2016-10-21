import java.util.Arrays;
import java.util.Random;
import javax.swing.JFrame;
import org.math.plot.Plot2DPanel;

public class BasicPlotting {
	public static void main(String[] args) {
		//int size = 100;
		
		/*double[] sample1 = new double[size];
		double[] sample2 = new double[size];
		
		for (int i = 0; i < size; i++) {
			sample2[i] = i;
			sample1[i] = 10;
		}*/
		
		CSVData myData = CSVData.readCSVFile("/Users/nehakonakalla/Documents/workspace/NoiseSmoothing/data/walkingSampleData-out.csv", 1, new String[] {"accel x", "accel y", "accel z", "gyro x", "gyro y", "gyro z", "linear accel x", "linear accel y", "linear accel z", "orientation x", "orientation y", "orientation z"});
		
		addNoise(myData.getCol("accel x"), 5); 
		addNoise(myData.getCol("accel y"), 50);
		
		Plot2DPanel plot = new Plot2DPanel();
		
		// add a line plot to the PlotPanel
		plot.addLinePlot("Random signal", myData.getCol("gyro x"));
		plot.addLinePlot("y = x + noise", myData.getCol("gyro y"));
		
		// put the PlotPanel in a JFrame, as a JPanel
		JFrame frame = new JFrame("Results");
		frame.setSize(800, 600);
		frame.setContentPane(plot);
		frame.setVisible(true);
	}

	private static void addNoise(double[] sample, int max) {
		for (int i = 0; i < sample.length; i++) {
			sample[i] += (-max + Math.random()*2*max);
		}
	}
}
