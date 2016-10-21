
public class StepCounterTester {
	
	public static void main (String[] args){
		
		CSVData myData = CSVData.readCSVFile("/Users/nehakonakalla/Documents/workspace/NoiseSmoothing/data/walkingSampleData-out.csv", 1, new String[] {"accel x", "accel y", "accel z", "gyro x", "gyro y", "gyro z", "linear accel x", "linear accel y", "linear accel z", "orientation x", "orientation y", "orientation z"});
		
		double[][] data = myData.get2DArray();
		
		double[][] smallerData = new double[data.length][6];
		
		double[] times = new double[data.length];
		
		for (int i = 0; i < data.length; i++){
			
			times[i] = data[i][0];
			
		}
		
		for (int row = 0; row < data.length; row++){
			
			for (int col = 1; col <= 6; col++){
				
				smallerData[row][col - 1] = data[row][col];
				
			}
			
		}
		
		
		
		
		System.out.println(StepCounter.countSteps(times,smallerData));
		
	}

}
